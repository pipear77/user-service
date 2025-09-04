package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repositories.ReactiveUsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        String,
        ReactiveUsuarioRepository
        > implements UsuarioRepository {

    private final TransactionalOperator txOperator;

    public UsuarioRepositoryAdapter(ReactiveUsuarioRepository repository,
                                    ObjectMapper mapper,
                                    TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.txOperator = txOperator;
    }

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        log.info("Guardando usuario: {}", usuario);
        return txOperator.transactional(super.save(usuario))
                .switchIfEmpty(Mono.error(() -> {
                    log.error("No se pudo guardar el usuario");
                    return new RuntimeException("No se pudo guardar el usuario");
                }));
    }

    @Override
    public Mono<Boolean> existsByEmail(String correoElectronico) {
        log.info("Verificando existencia por correo: {}", correoElectronico);
        return repository.existsByCorreoElectronico(correoElectronico)
                .doOnNext(existe -> log.info("¿Existe? {}", existe));
    }

    @Override
    public Mono<Boolean> existsByDocumentNumber(String numeroDocumento) {
        log.info("Verificando existencia por documento: {}", numeroDocumento);
        return repository.existsByNumeroDocumento(numeroDocumento)
                .doOnNext(existe -> log.info("¿Existe? {}", existe));
    }

    @Override
    public Flux<Usuario> findAllUsuarios() {
        return repository.findAll()
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Mono<Usuario> findByCorreoElectronico(String correoElectronico) {
        log.info("Buscando usuario por correo: {}", correoElectronico);
        return repository.findByCorreoElectronico(correoElectronico)
                .map(entity -> {
                    log.info("Entidad recuperada: {}", entity);
                    Usuario usuario = mapper.map(entity, Usuario.class);
                    log.info("Usuario mapeado: {}", usuario);
                    log.info("Contraseña mapeada: {}", usuario.getContrasena());
                    return usuario;
                })
                .doOnNext(usuario -> log.info("Usuario final: {}", usuario.getId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }


}
