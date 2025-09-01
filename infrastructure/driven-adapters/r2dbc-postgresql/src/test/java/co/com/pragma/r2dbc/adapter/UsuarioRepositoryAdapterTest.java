package co.com.pragma.r2dbc.adapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.mapper.UsuarioMapper;
import co.com.pragma.r2dbc.repository.ReactiveUsuarioDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    ReactiveUsuarioDataRepository dataRepository;

    @Mock
    UsuarioMapper mapper;

    @InjectMocks
    UsuarioRepositoryAdapter adapter;

    @Test
    void mustSaveUsuario() {
        Usuario usuario = Usuario.builder().id("1").build();
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId("1");

        when(mapper.toEntity(usuario)).thenReturn(entity);
        when(dataRepository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(usuario);

        Mono<Usuario> result = adapter.save(usuario);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void mustFindByCorreoElectronico() {
        String correo = "test@correo.com";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId("1");

        Usuario usuario = Usuario.builder().id("1").build();

        when(dataRepository.findByCorreoElectronico(correo)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(usuario);

        Mono<Usuario> result = adapter.findByCorreoElectronico(correo);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void mustFindAllUsuarios() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId("1");

        Usuario usuario = Usuario.builder().id("1").build();

        when(dataRepository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toDomain(entity)).thenReturn(usuario);

        Flux<Usuario> result = adapter.findAllUsuarios();

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getId().equals("1"))
                .verifyComplete();
    }

}