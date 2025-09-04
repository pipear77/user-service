package co.com.pragma.model.rol.gateways;

import co.com.pragma.model.rol.Rol;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RolRepository {
    Mono<Rol> findById(UUID id);
}
