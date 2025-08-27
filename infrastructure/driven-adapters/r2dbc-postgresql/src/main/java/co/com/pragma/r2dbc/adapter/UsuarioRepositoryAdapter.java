package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.com.pragma.r2dbc.mapper.UsuarioMapper;
import co.com.pragma.r2dbc.repository.ReactiveUsuarioDataRepository;

@RequiredArgsConstructor
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {
    private final ReactiveUsuarioDataRepository dataRepository;
    private final UsuarioMapper mapper;


    @Override
    public Mono<Usuario> save(Usuario usuario) {
        return dataRepository.save(mapper.toEntity(usuario))
                .map(mapper::toDomain);

    }

    @Override
    public Mono<Usuario> findByCorreoElectronico(String correoElectronico) {
        return dataRepository.findByCorreoElectronico(correoElectronico)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Usuario> findAllUsuarios() {
        return dataRepository.findAll()
                .map(mapper::toDomain);
    }

}
