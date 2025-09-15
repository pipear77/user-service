package co.com.pragma.api.auth;

import co.com.pragma.api.dto.LoginRequestDTO;
import co.com.pragma.api.dto.UsuarioAutenticadoDTO;
import co.com.pragma.api.exceptions.ErrorInesperadoException;
import co.com.pragma.r2dbc.common.Constantes;
import co.com.pragma.usecase.dto.ErrorDto;
import co.com.pragma.usecase.login.LoginUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Set;

import static co.com.pragma.r2dbc.common.Constantes.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final LoginUseCase loginUseCase;
    private final Validator validator;

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("Procesando solicitud de login");

        return request.bodyToMono(LoginRequestDTO.class)
                .flatMap(this::validate)
                .flatMap(dto -> loginUseCase.login(dto.getCorreoElectronico(), dto.getContrasena()))
                .doOnNext(token -> log.info("Token generado correctamente para {}", request.path()))
                .flatMap(token -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(token))
                .onErrorResume(e -> {
                    // Diferenciar los errores
                    if (e instanceof IllegalArgumentException || e instanceof ConstraintViolationException) {
                        log.warn("Credenciales inválidas: {}", e.getMessage());
                        return ServerResponse.status(401)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorDto("Credenciales inválidas", 401));
                    }
                    log.error("Error inesperado en login", e);
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto("Error interno. Intenta más tarde.", 500));
                });
    }

    public Mono<ServerResponse> validateToken(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Token no presente o mal formado");
            return ServerResponse.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorDto("Token requerido o mal formado", 401));
        }

        String token = authHeader.substring(7).trim();
        log.debug("Token recibido: {}", token);

        return Mono.fromCallable(() -> {
                    try {
                        String id = loginUseCase.getJwtProvider().getClaim(token, "id");
                        String correo = loginUseCase.getJwtProvider().getSubject(token);
                        String documento = loginUseCase.getJwtProvider().getClaim(token, "documento");
                        String rol = loginUseCase.getJwtProvider().getClaim(token, "rol");
                        String nombres = loginUseCase.getJwtProvider().getClaim(token, "nombres");
                        String apellidos = loginUseCase.getJwtProvider().getClaim(token, "apellidos");
                        String salarioBaseStr = loginUseCase.getJwtProvider().getClaim(token, "salarioBase");

                        return UsuarioAutenticadoDTO.builder()
                                .id(id)
                                .correo(correo)
                                .documentoIdentidad(documento)
                                .nombres(nombres)
                                .apellidos(apellidos)
                                .rol(rol)
                                .estado("ACTIVO")
                                .sesionActiva(true)
                                .salarioBase(new BigDecimal(salarioBaseStr))
                                .build();
                    } catch (Exception e) {
                        log.warn("Token inválido o expirado: {}", e.getMessage());
                        throw new IllegalArgumentException("Token inválido o expirado");
                    }
                })
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return ServerResponse.status(401)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorDto("Token inválido o expirado", 401));
                    }
                    log.error("Error inesperado al validar token", e);
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto("Error interno. Intenta más tarde.", 500));
                });
    }

    private <T> Mono<T> validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            String mensaje = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Datos inválidos");
            log.warn("Validación fallida: {}", mensaje);
            return Mono.error(new IllegalArgumentException(mensaje));
        }
        return Mono.just(bean);
    }
}

