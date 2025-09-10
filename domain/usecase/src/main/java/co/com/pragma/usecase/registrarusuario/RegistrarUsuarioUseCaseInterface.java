package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RegistrarUsuarioUseCaseInterface {
    public Mono<Usuario> save(Usuario usuario);
    public Flux<Usuario> getAllUsuarios();
    public Mono<Boolean> existsByCorreo(String email);
    public Mono<Boolean> existsByDocumentNumber(String documentNumber);
    Mono<Usuario> getUsuarioPorDocumento(String documentoIdentidad);


}
