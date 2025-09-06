package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.repositories.ReactiveUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    ReactiveUsuarioRepository dataRepository;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    TransactionalOperator txOperator;

    @InjectMocks
    UsuarioRepositoryAdapter adapter;

    @Test
    void mustSaveUsuario() {
        UUID id = UUID.randomUUID();

        Usuario usuario = Usuario.builder().id(id).build();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).build();

        when(objectMapper.map(usuario, UsuarioEntity.class)).thenReturn(entity);
        when(dataRepository.save(entity)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Usuario.class)).thenReturn(usuario);
        when(txOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mono<Usuario> result = adapter.save(usuario);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getId().equals(id))
                .verifyComplete();

        verify(dataRepository).save(entity);
        verify(objectMapper).map(usuario, UsuarioEntity.class);
        verify(objectMapper).map(entity, Usuario.class);
    }

    @Test
    void mustFindAllUsuarios() {
        UUID id = UUID.randomUUID();

        UsuarioEntity entity = UsuarioEntity.builder().id(id).build();
        Usuario usuario = Usuario.builder().id(id).build();

        when(dataRepository.findAll()).thenReturn(Flux.just(entity));
        when(objectMapper.map(entity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = adapter.findAllUsuarios();

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getId().equals(id))
                .verifyComplete();

        verify(dataRepository).findAll();
        verify(objectMapper).map(entity, Usuario.class);
    }

    @Test
    void mustCheckExistenceByEmail() {
        String correo = "test@correo.com";

        when(dataRepository.existsByCorreoElectronico(correo)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByEmail(correo))
                .expectNext(true)
                .verifyComplete();

        verify(dataRepository).existsByCorreoElectronico(correo);
    }

    @Test
    void mustCheckExistenceByDocumentNumber() {
        String documento = "123456789";

        when(dataRepository.existsByNumeroDocumento(documento)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByDocumentNumber(documento))
                .expectNext(false)
                .verifyComplete();

        verify(dataRepository).existsByNumeroDocumento(documento);
    }
}