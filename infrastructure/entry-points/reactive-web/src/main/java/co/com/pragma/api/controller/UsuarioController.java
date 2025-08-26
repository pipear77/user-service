package co.com.pragma.api.controller;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarUsuario(@RequestBody Usuario usuario) {
        return registrarUsuarioUseCase.registrarUsuario(usuario)
                .map(u -> ResponseEntity.status(201).body((Object) u))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.badRequest().body(Map.of("error", e.getMessage()))
                ));
    }
}
