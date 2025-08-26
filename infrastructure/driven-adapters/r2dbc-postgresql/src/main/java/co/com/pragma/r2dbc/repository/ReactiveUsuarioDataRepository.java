package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUsuarioDataRepository extends ReactiveCrudRepository<UsuarioEntity, String> {
    Mono<UsuarioEntity> findByCorreoElectronico(String correoElectronico);
}

