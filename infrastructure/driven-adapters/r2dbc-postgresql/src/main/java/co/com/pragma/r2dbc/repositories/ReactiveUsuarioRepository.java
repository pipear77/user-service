package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUsuarioRepository extends ReactiveCrudRepository<UsuarioEntity, UUID>, ReactiveQueryByExampleExecutor<UsuarioEntity> {
    Mono<UsuarioEntity> findByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByNumeroDocumento(String documentId);
    Mono<Boolean> existsByCorreoElectronico(String correo);
    Mono<UsuarioEntity> findByNumeroDocumento(String numeroDocumento);
}

