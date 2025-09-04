package co.com.pragma.r2dbc.repositories;

import co.com.pragma.r2dbc.entity.RolEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveRolRepository extends ReactiveCrudRepository<RolEntity, UUID>, ReactiveQueryByExampleExecutor<RolEntity> {
    Mono<RolEntity> findById(UUID id);
}
