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
    private final ModelMapper modelMapper;

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("Procesando solicitud de login");

        return request.bodyToMono(LoginRequestDTO.class)
                .flatMap(this::validate)
                .flatMap(dto -> loginUseCase.login(dto.getCorreoElectronico(), dto.getContrasena()))
                .doOnNext(token -> log.info("Token generado correctamente"))
                .flatMap(token -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(token))
                .onErrorResume(e -> {
                    log.error("Error en login: {}", e.getMessage());
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto(DESCRIPCION_ERROR_INESPERADO, 500));
                });
    }

    public Mono<ServerResponse> validateToken(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("{}", TOKEN_MALFORMADO);
            return ServerResponse.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorDto(TOKEN_REQUERIDO, 401));
        }

        String token = authHeader.substring(7).trim();
        log.info("Token limpio recibido");

        if (!token.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")) {
            log.warn("Token con formato inválido");
            return ServerResponse.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorDto("Token mal formado", 401));
        }

        return Mono.fromCallable(() -> {
                    try {
                        String id = loginUseCase.getJwtProvider().getClaim(token, "id");
                        String correo = loginUseCase.getJwtProvider().getSubject(token);
                        String documento = loginUseCase.getJwtProvider().getClaim(token, "documento");
                        String rol = loginUseCase.getJwtProvider().getClaim(token, "rol");
                        String nombres = loginUseCase.getJwtProvider().getClaim(token, "nombres");
                        String apellidos = loginUseCase.getJwtProvider().getClaim(token, "apellidos");
                        String salarioBaseStr = loginUseCase.getJwtProvider().getClaim(token, "salarioBase");

                        log.info("Claims extraídos correctamente");

                        BigDecimal salarioBase = new BigDecimal(salarioBaseStr);

                        return UsuarioAutenticadoDTO.builder()
                                .id(id)
                                .correo(correo)
                                .documentoIdentidad(documento)
                                .nombres(nombres)
                                .apellidos(apellidos)
                                .rol(rol)
                                .estado("ACTIVO")
                                .sesionActiva(true)
                                .salarioBase(salarioBase)
                                .build();
                    } catch (Exception e) {
                        log.error("{}", ERROR_INESPERADO, e);
                        throw new ErrorInesperadoException();
                    }
                })
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .onErrorResume(e -> {
                    log.warn("Error al validar token: {}", e.getMessage());
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto(DESCRIPCION_ERROR_INESPERADO, 500));
                });
    }

    private <T> Mono<T> validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            log.warn("Validación fallida: {}", violations);
            return Mono.error(new ConstraintViolationException(violations));
        }
        return Mono.just(bean);
    }
}
