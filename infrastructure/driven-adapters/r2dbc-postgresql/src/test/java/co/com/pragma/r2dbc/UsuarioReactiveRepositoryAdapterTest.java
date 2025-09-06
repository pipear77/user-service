package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.adapter.UsuarioRepositoryAdapter;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.repositories.ReactiveUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioReactiveRepositoryAdapterTest {

    private UsuarioRepositoryAdapter repositoryAdapter;

    private ReactiveUsuarioRepository repository;
    private ObjectMapper mapper;
    private TransactionalOperator txOperator;

    @BeforeEach
    void setUp() {
        repository = mock(ReactiveUsuarioRepository.class);
        mapper = mock(ObjectMapper.class);
        txOperator = mock(TransactionalOperator.class);

        when(txOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        repositoryAdapter = new UsuarioRepositoryAdapter(repository, mapper, txOperator);
    }

    @Test
    void mustFindValueById() {
        UUID id = UUID.randomUUID();

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);

        Usuario usuario = Usuario.builder().id(id).build();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(id))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UUID id = UUID.randomUUID();

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);

        Usuario usuario = Usuario.builder().id(id).build();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(id))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        UUID id = UUID.randomUUID();

        Usuario usuarioEjemplo = Usuario.builder().id(id).build();
        UsuarioEntity entityEjemplo = new UsuarioEntity();
        entityEjemplo.setId(id);

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);

        Usuario usuario = Usuario.builder().id(id).build();

        when(mapper.map(usuarioEjemplo, UsuarioEntity.class)).thenReturn(entityEjemplo);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findByExample(usuarioEjemplo);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(id))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void mustSaveValue() {
        UUID id = UUID.randomUUID();

        Usuario usuario = Usuario.builder().id(id).build();
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);

        when(mapper.map(usuario, UsuarioEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(id))
                .verifyComplete();
    }
}