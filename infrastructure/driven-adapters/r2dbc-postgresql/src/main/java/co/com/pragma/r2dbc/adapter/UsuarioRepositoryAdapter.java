package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.r2dbc.common.Constantes;
import co.com.pragma.r2dbc.common.exceptions.ErrorTecnicoUsuarioException;
import co.com.pragma.r2dbc.common.exceptions.UsuarioNoEncontradoException;
import co.com.pragma.r2dbc.common.exceptions.UsuarioNoGuardadoException;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repositories.ReactiveUsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        UUID,
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
        log.info("📝 Guardando usuario con correo: {}", usuario.getCorreo());
        return txOperator.transactional(super.save(usuario))
                .switchIfEmpty(Mono.error(() -> {
                    log.error("❌ {}", Constantes.USUARIO_NO_GUARDADO);
                    return new UsuarioNoGuardadoException();
                }));
    }

    @Override
    public Mono<Boolean> existsByCorreo(String correoElectronico) {
        log.info("🔍 Verificando existencia por correo: {}", correoElectronico);
        return repository.existsByCorreo(correoElectronico)
                .doOnNext(existe -> log.info("📌 ¿Existe correo? {}", existe));
    }

    @Override
    public Mono<Boolean> existsByDocumentNumber(String numeroDocumento) {
        log.info("🔍 Verificando existencia por documento: {}", numeroDocumento);
        return repository.existsByNumeroDocumento(numeroDocumento)
                .doOnNext(existe -> log.info("📌 ¿Existe documento? {}", existe));
    }

    @Override
    public Flux<Usuario> findAllUsuarios() {
        log.info("📋 Recuperando todos los usuarios");
        return repository.findAll()
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Mono<Usuario> findByCorreo(String correo) {
        log.info("🔍 Buscando usuario por correo: {}", correo);
        return repository.findByCorreo(correo)
                .map(entity -> {
                    log.info("📦 Entidad recuperada con ID: {}", entity.getId());
                    Usuario usuario = mapper.map(entity, Usuario.class);
                    log.info("✅ Usuario mapeado con ID: {}", usuario.getId());
                    return usuario;
                })
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException()));
    }

    @Override
    public Mono<Usuario> findByNumeroDocumento(String documentNumber) {
        log.info("🔍 Buscando usuario por documento: {}", documentNumber);
        return repository.findByNumeroDocumento(documentNumber)
                .doOnNext(entity -> log.info("📦 Entidad recuperada con ID: {}", entity.getId()))
                .map(entity -> mapper.map(entity, Usuario.class))
                .doOnNext(usuario -> log.info("✅ Usuario mapeado con ID: {}", usuario.getId()))
                .onErrorResume(e -> {
                    log.error("❌ {}", Constantes.ERROR_TECNICO_CONSULTA_USUARIO, e);
                    return Mono.error(new ErrorTecnicoUsuarioException());
                });
    }
}
