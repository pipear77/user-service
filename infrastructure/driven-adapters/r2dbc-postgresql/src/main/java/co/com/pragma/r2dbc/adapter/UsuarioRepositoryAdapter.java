package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.mapper.UsuarioMapper;
import co.com.pragma.r2dbc.repository.ReactiveUsuarioDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final ReactiveUsuarioDataRepository dataRepository;
    private final UsuarioMapper mapper;
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        log.info("Guardando usuario con ID: {}", usuario.getId());

        UsuarioEntity entity = mapper.toEntity(usuario);

        return entityTemplate.insert(UsuarioEntity.class)
                .using(entity)
                .map(mapper::toDomain)
                .doOnNext(u -> log.info("Usuario insertado correctamente: {}", u));
    }

    @Override
    public Mono<Usuario> findByCorreoElectronico(String correoElectronico) {
        log.info("Buscando usuario por correo: {}", correoElectronico);
        return dataRepository.findByCorreoElectronico(correoElectronico)
                .map(mapper::toDomain)
                .doOnNext(u -> log.info("Usuario encontrado: {}", u));
    }

    @Override
    public Flux<Usuario> findAllUsuarios() {
        log.info("Listando todos los usuarios");
        return dataRepository.findAll()
                .map(mapper::toDomain);
    }
}
