package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerTest {

    private RegistrarUsuarioUseCase useCase;
    private Handler handler;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(RegistrarUsuarioUseCase.class);
        handler = new Handler(useCase);
    }

    @Test
    void save_shouldReturnCreatedResponse() {
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .telefono("123456789")
                .correoElectronico("juan@correo.com")
                .salarioBase(BigDecimal.valueOf(50000))
                .build();

        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .telefono("123456789")
                .correoElectronico("juan@correo.com")
                .salarioBase(BigDecimal.valueOf(50000))
                .build();

        ServerRequest mockRequest = Mockito.mock(ServerRequest.class);
        when(mockRequest.bodyToMono(UsuarioRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(useCase.save(any(Usuario.class))).thenReturn(Mono.just(usuario));

        Mono<ServerResponse> responseMono = handler.save(mockRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().value() == 201)
                .verifyComplete();
    }


    @Test
    void getAll_shouldReturnOkResponseWithList() {
        Usuario usuario1 = Usuario.builder().id(UUID.randomUUID()).nombres("Ana").build();
        Usuario usuario2 = Usuario.builder().id(UUID.randomUUID()).nombres("Luis").build();

        when(useCase.getAllUsuarios()).thenReturn(Flux.just(usuario1, usuario2));

        ServerRequest mockRequest = Mockito.mock(ServerRequest.class);

        Mono<ServerResponse> responseMono = handler.getAll(mockRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

}