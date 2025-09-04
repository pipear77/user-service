package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.r2dbc.entity.RolEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repositories.ReactiveRolRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Repository
public class RolRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        UUID,
        ReactiveRolRepository
        > implements RolRepository {

    public RolRepositoryAdapter(ReactiveRolRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Rol.class));
    }

    @Override
    public Mono<Rol> findById(UUID id) {
        log.info("Buscando rol por ID: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("RolEntity recuperado: {}", entity);
                    Rol rol = mapper.map(entity, Rol.class);
                    log.info("Rol mapeado: {}", rol);
                    return rol;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado")));
    }
}
