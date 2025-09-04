package co.com.pragma.api;

import co.com.pragma.api.auth.AuthHandler;
import co.com.pragma.api.config.TestSecurityConfig;
import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import co.com.pragma.api.dto.LoginRequestDTO;
import co.com.pragma.usecase.dto.TokenResponseDTO;
import co.com.pragma.usecase.login.LoginUseCase;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest
@Import({RouterRest.class, RouterRestTest.TestConfig.class, TestSecurityConfig.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Handler handler;

    @Autowired
    private AuthHandler authHandler;

    @Configuration
    static class TestConfig {

        @Bean
        public Handler handler() {
            RegistrarUsuarioUseCase useCase = Mockito.mock(RegistrarUsuarioUseCase.class);
            return Mockito.mock(Handler.class);
        }

        @Bean
        public AuthHandler authHandler() {
            LoginUseCase loginUseCase = Mockito.mock(LoginUseCase.class);
            return Mockito.mock(AuthHandler.class);
        }
    }

    @Test
    void testPOSTUsuarioRoute() {
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Juan")
                .correoElectronico("juan@correo.com")
                .build();

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .id("abc123")
                .nombres("Juan")
                .build();

        Mockito.when(handler.save(any()))
                .thenReturn(ServerResponse.created(null).bodyValue(responseDTO));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(requestDTO))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testGETUsuarioRoute() {
        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .id("abc123")
                .nombres("Juan")
                .build();

        Mockito.when(handler.getAll(any()))
                .thenReturn(ServerResponse.ok().bodyValue(responseDTO));

        webTestClient.get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testPOSTLoginRoute() {
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .correoElectronico("leidy@example.com")
                .contrasena("claveSegura123")
                .build();

        TokenResponseDTO tokenResponse = TokenResponseDTO.builder()
                .token("jwt-token-generado")
                .tipo("Bearer")
                .expiracion(System.currentTimeMillis() + 3600000)
                .build();

        Mockito.when(authHandler.login(any()))
                .thenReturn(ServerResponse.ok().bodyValue(tokenResponse));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(loginRequest))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testPOSTUsuarioRoute_withValidToken_shouldReturnCreated() {
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Juan")
                .correoElectronico("juan@correo.com")
                .build();

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .id("abc123")
                .nombres("Juan")
                .build();

        Mockito.when(handler.save(any()))
                .thenReturn(ServerResponse.created(null).bodyValue(responseDTO));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(requestDTO))
                .exchange()
                .expectStatus().isCreated();
    }

}
