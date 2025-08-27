package co.com.pragma.api.controller;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @PostMapping
    public Mono<ResponseEntity<Usuario>> save(@RequestBody Usuario usuario) {
        return registrarUsuarioUseCase.save(usuario)
                .map(u -> ResponseEntity.status(201).body(u))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }


    @GetMapping
    public Mono<ResponseEntity<List<Usuario>>> getAllUsuarios() {
        return registrarUsuarioUseCase.getAllUsuarios()
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.badRequest().body(List.of()))
                );
    }

}
