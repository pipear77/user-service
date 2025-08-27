package co.com.pragma.api;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {
    private final RegistrarUsuarioUseCase useCase;

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(Usuario.class)
                .flatMap(useCase::save)
                .flatMap(usuario -> ServerResponse.status(201).bodyValue(usuario))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return useCase.getAllUsuarios()
                .collectList()
                .flatMap(lista -> ServerResponse.ok().bodyValue(lista))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(List.of()));
    }


}
