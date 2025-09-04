package co.com.pragma.usecase.common;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.common.validacion.UsuarioValidacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class UsuarioValidationPipeline {
    private final List<UsuarioValidacion> validaciones = new ArrayList<>();

    public UsuarioValidationPipeline agregarValidacion(UsuarioValidacion validacion) {
        validaciones.add(validacion);

        return this;
    }

    public Mono<Void> validar(Usuario usuario) {
        return Flux.fromIterable(validaciones)
                .concatMap(validacion -> {
                    Mono<Void> resultado = validacion.validar(usuario);
                    return resultado != null ? resultado : Mono.error(new RuntimeException("La validación retornó null"));
                })
                .then(); // ← se completa si todas las validaciones pasan
    }

}
