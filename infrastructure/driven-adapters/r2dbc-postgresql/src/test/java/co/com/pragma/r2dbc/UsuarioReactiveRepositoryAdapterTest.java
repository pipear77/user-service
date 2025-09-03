package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.service.UsuarioReactiveRepository;
import co.com.pragma.r2dbc.service.UsuarioReactiveRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioReactiveRepositoryAdapterTest {

    @InjectMocks
    UsuarioReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UUID uuid = UUID.randomUUID();

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(uuid.toString());

        Usuario usuario = Usuario.builder().id(uuid).build();

        when(repository.findById(uuid.toString())).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById(uuid.toString());

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(uuid))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UUID uuid = UUID.randomUUID();

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(uuid.toString());

        Usuario usuario = Usuario.builder().id(uuid).build();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(uuid))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void mustSaveValue() {
        UUID uuid = UUID.randomUUID();

        Usuario usuario = Usuario.builder().id(uuid).build();
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(uuid.toString());

        when(mapper.map(usuario, UsuarioEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(uuid))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        UUID uuid = UUID.randomUUID();

        Usuario usuarioEjemplo = Usuario.builder().id(uuid).build();

        UsuarioEntity entityEjemplo = new UsuarioEntity();
        entityEjemplo.setId(uuid.toString());

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(uuid.toString());

        Usuario usuario = Usuario.builder().id(uuid).build();

        when(mapper.map(usuarioEjemplo, UsuarioEntity.class)).thenReturn(entityEjemplo);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findByExample(usuarioEjemplo);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(uuid))
                .verifyComplete();
    }
}
