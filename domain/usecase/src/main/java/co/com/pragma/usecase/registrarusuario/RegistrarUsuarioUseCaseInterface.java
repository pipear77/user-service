package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface RegistrarUsuarioUseCaseInterface {
    public Mono<Usuario> registrarUsuario(Usuario usuario);
}
