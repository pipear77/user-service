package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.mapper.IUsuarioRequestMapper;
import co.com.pragma.api.mapper.IUsuarioResponseMapper;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
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
public class Handler {

    private final RegistrarUsuarioUseCase useCase;
    private final IUsuarioRequestMapper requestMapper;
    private final IUsuarioResponseMapper responseMapper;
    private final Validator validator;

    public Mono<ServerResponse> save(ServerRequest request) {
        log.info("Escuchando solicitud de creación de usuario");

        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnNext(dto -> log.debug("DTO recibido: {}", dto))
                .flatMap(this::validate)
                .map(requestMapper::toUsuario)
                .doOnNext(usuario -> log.debug("Mapeado a dominio: {}", usuario))
                .flatMap(useCase::save)
                .doOnNext(saved -> log.info("Usuario guardado exitosamente: {}", saved.getId()))
                .map(responseMapper::toResponseDTO)
                .doOnNext(dto -> log.debug("DTO de respuesta generado: {}", dto))
                .flatMap(dto -> ServerResponse.created(request.uri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("Escuchando solicitud de consulta de todos los usuarios");

        return useCase.getAllUsuarios()
                .map(responseMapper::toResponseDTO)
                .collectList()
                .doOnNext(lista -> log.info("Total usuarios recuperados: {}", lista.size()))
                .flatMap(lista -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(lista));
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
