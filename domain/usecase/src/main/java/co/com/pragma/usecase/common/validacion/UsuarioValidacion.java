package co.com.pragma.usecase.common.validacion;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioValidacion {
    Mono<Void> validar(Usuario user);
}
