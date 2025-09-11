package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.mapper.IUsuarioRequestMapper;
import co.com.pragma.api.mapper.IUsuarioResponseMapper;
import co.com.pragma.r2dbc.common.Constantes;
import co.com.pragma.usecase.dto.ErrorDto;
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

import static co.com.pragma.r2dbc.common.Constantes.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final RegistrarUsuarioUseCase useCase;
    private final IUsuarioRequestMapper requestMapper;
    private final IUsuarioResponseMapper responseMapper;
    private final Validator validator;

    public Mono<ServerResponse> save(ServerRequest request) {
        log.info("Solicitud de creación de usuario recibida");

        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("{}", TOKEN_MALFORMADO);
            return ServerResponse.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorDto(TOKEN_REQUERIDO, 401));
        }

        String token = authHeader.substring(7);
        String rol = useCase.getJwtProvider().getClaim(token, "rol");

        if (!rol.equals("ROL_ADMIN") && !rol.equals("ROL_ASESOR")) {
            log.warn("{}", ROL_NO_AUTORIZADO);
            return ServerResponse.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorDto(ROL_NO_AUTORIZADO, 403));
        }

        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnNext(dto -> log.debug("DTO recibido: {}", dto))
                .flatMap(this::validate)
                .map(requestMapper::toUsuario)
                .doOnNext(usuario -> log.debug("🔧 Mapeado a dominio: {}", usuario))
                .flatMap(useCase::save)
                .doOnNext(saved -> log.info("Usuario guardado: {}", saved.getId()))
                .map(responseMapper::toResponseDTO)
                .doOnNext(dto -> log.debug("DTO de respuesta: {}", dto))
                .flatMap(dto -> ServerResponse.created(request.uri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("Solicitud de consulta de todos los usuarios");

        return useCase.getAllUsuarios()
                .map(responseMapper::toResponseDTO)
                .collectList()
                .doOnNext(lista -> log.info("Total usuarios recuperados: {}", lista.size()))
                .flatMap(lista -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(lista))
                .onErrorResume(e -> {
                    log.error("{}", ERROR_CONSULTA_USUARIOS, e);
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto(ERROR_CONSULTA_USUARIOS, 500));
                });
    }

    public Mono<ServerResponse> getByDocumento(ServerRequest request) {
        String documento = request.pathVariable("documento");
        log.info("📡 Buscando usuario con documento: {}", documento);

        return useCase.getUsuarioPorDocumento(documento)
                .map(responseMapper::toResponseDTO)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .switchIfEmpty(ServerResponse.status(404)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorDto(USUARIO_NO_ENCONTRADO_POR_DOCUMENTO + documento, 404)))
                .onErrorResume(e -> {
                    log.error("{}", ERROR_CONSULTA_USUARIO_POR_DOCUMENTO, e);
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorDto(ERROR_CONSULTA_USUARIO_POR_DOCUMENTO, 500));
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
