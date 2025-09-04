package co.com.pragma.api.auth;

import co.com.pragma.api.dto.LoginRequestDTO;
import co.com.pragma.usecase.login.LoginUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
