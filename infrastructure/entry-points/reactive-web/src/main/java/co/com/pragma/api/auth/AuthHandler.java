package co.com.pragma.api.auth;

import co.com.pragma.api.dto.LoginRequestDTO;
import co.com.pragma.api.dto.UsuarioAutenticadoDTO;
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

import java.util.Set;

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
                        .bodyValue(token));
    }

    private <T> Mono<T> validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            log.warn("Validación fallida: {}", violations);
            return Mono.error(new ConstraintViolationException(violations));
        }
        return Mono.just(bean);
    }

    public Mono<ServerResponse> validateToken(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Token no presente o mal formado");
            return ServerResponse.status(401).bodyValue("Token de autorización requerido");
        }

        String token = authHeader.substring(7);

        return Mono.fromCallable(() -> {
                    UsuarioAutenticadoDTO dto = new UsuarioAutenticadoDTO();
                    dto.setCorreo(loginUseCase.getJwtProvider().getSubject(token));
                    dto.setDocumentoIdentidad(loginUseCase.getJwtProvider().getClaim(token, "documento"));
                    dto.setRol(loginUseCase.getJwtProvider().getClaim(token, "rol"));
                    dto.setEstado("ACTIVO"); // puedes ajustar si tienes campo real
                    dto.setSesionActiva(true); // puedes ajustar si tienes lógica de sesión
                    return dto;
                })
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .onErrorResume(e -> {
                    log.warn("Error al validar token: {}", e.getMessage());
                    return ServerResponse.status(401).bodyValue("Token inválido o expirado");
                });
    }
}
