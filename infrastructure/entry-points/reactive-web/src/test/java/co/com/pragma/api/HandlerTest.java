package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import co.com.pragma.api.mapper.IUsuarioRequestMapper;
import co.com.pragma.api.mapper.IUsuarioResponseMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerTest {

    private RegistrarUsuarioUseCase useCase;
    private IUsuarioRequestMapper requestMapper;
    private IUsuarioResponseMapper responseMapper;
    private Validator validator;
    private Handler handler;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(RegistrarUsuarioUseCase.class);
        requestMapper = Mockito.mock(IUsuarioRequestMapper.class);
        responseMapper = Mockito.mock(IUsuarioResponseMapper.class);
        validator = Mockito.mock(Validator.class);

        handler = new Handler(useCase, requestMapper, responseMapper, validator);
    }

    @Test
    void getAll_shouldReturnOkResponseWithList() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Usuario usuario1 = Usuario.builder().id(id1).nombres("Ana").build();
        Usuario usuario2 = Usuario.builder().id(id2).nombres("Luis").build();

        UsuarioResponseDTO dto1 = UsuarioResponseDTO.builder().id(id1.toString()).nombres("Ana").build();
        UsuarioResponseDTO dto2 = UsuarioResponseDTO.builder().id(id2.toString()).nombres("Luis").build();

        when(useCase.getAllUsuarios()).thenReturn(Flux.just(usuario1, usuario2));
        when(responseMapper.toResponseDTO(usuario1)).thenReturn(dto1);
        when(responseMapper.toResponseDTO(usuario2)).thenReturn(dto2);

        ServerRequest mockRequest = Mockito.mock(ServerRequest.class);

        Mono<ServerResponse> responseMono = handler.getAll(mockRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void save_shouldReturnForbiddenIfRoleIsNotAdminOrAsesor() {
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Juan")
                .correoElectronico("juan@correo.com")
                .build();

        JwtProviderRepository jwtProvider = Mockito.mock(JwtProviderRepository.class);
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoderRepository passwordEncoderRepository = Mockito.mock(PasswordEncoderRepository.class);

        RegistrarUsuarioUseCase useCaseConJwt = new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoderRepository, jwtProvider);

        IUsuarioRequestMapper requestMapper = Mockito.mock(IUsuarioRequestMapper.class);
        IUsuarioResponseMapper responseMapper = Mockito.mock(IUsuarioResponseMapper.class);
        Validator validator = Mockito.mock(Validator.class);

        Handler handlerConJwt = new Handler(useCaseConJwt, requestMapper, responseMapper, validator);

        ServerRequest mockRequest = Mockito.mock(ServerRequest.class);
        ServerRequest.Headers mockHeaders = Mockito.mock(ServerRequest.Headers.class);

        when(mockRequest.headers()).thenReturn(mockHeaders);
        when(mockHeaders.firstHeader("Authorization")).thenReturn("Bearer fake-token");
        when(mockRequest.bodyToMono(UsuarioRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(jwtProvider.getClaim("fake-token", "rol")).thenReturn("ROL_CLIENTE");

        Mono<ServerResponse> responseMono = handlerConJwt.save(mockRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().value() == 403)
                .verifyComplete();
    }

   /* @Test
    void save_shouldReturnCreatedResponse() {
        UUID id = UUID.randomUUID();

        // DTO de entrada
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .telefono("123456789")
                .correoElectronico("juan@correo.com")
                .salarioBase(BigDecimal.valueOf(50000))
                .build();

        // Dominio
        Usuario usuario = Usuario.builder()
                .id(id)
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .telefono("123456789")
                .correoElectronico("juan@correo.com")
                .salarioBase(BigDecimal.valueOf(50000))
                .build();

        // DTO de respuesta
        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .id(id.toString())
                .nombres("Juan")
                .build();

        // Mock del token y headers
        ServerRequest mockRequest = Mockito.mock(ServerRequest.class);
        ServerRequest.Headers mockHeaders = Mockito.mock(ServerRequest.Headers.class);
        when(mockRequest.headers()).thenReturn(mockHeaders);
        when(mockHeaders.firstHeader("Authorization")).thenReturn("Bearer fake-token");

        // Mock del JwtProviderRepository
        JwtProviderRepository jwtProvider = Mockito.mock(JwtProviderRepository.class);
        when(jwtProvider.getClaim("fake-token", "rol")).thenReturn("ROL_ADMIN");

        // Mock del useCase con JwtProvider inyectado
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        PasswordEncoderRepository passwordEncoderRepository = Mockito.mock(PasswordEncoderRepository.class);
        RegistrarUsuarioUseCase useCaseConJwt = new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoderRepository, jwtProvider);

        // Mapper y validator
        IUsuarioRequestMapper requestMapper = Mockito.mock(IUsuarioRequestMapper.class);
        IUsuarioResponseMapper responseMapper = Mockito.mock(IUsuarioResponseMapper.class);
        Validator validator = Mockito.mock(Validator.class);

        Handler handlerConJwt = new Handler(useCaseConJwt, requestMapper, responseMapper, validator);

        // Flujo de mocks
        when(mockRequest.bodyToMono(UsuarioRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(validator.validate(requestDTO)).thenReturn(Collections.emptySet());
        when(requestMapper.toUsuario(requestDTO)).thenReturn(usuario);
        when(useCaseConJwt.save(usuario)).thenReturn(Mono.just(usuario));
        when(responseMapper.toResponseDTO(usuario)).thenReturn(responseDTO);

        // Ejecutar
        Mono<ServerResponse> responseMono = handlerConJwt.save(mockRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().value() == 201)
                .verifyComplete();
    }*/

}
