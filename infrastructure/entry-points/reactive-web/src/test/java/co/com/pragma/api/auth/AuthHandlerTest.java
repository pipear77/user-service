/*
package co.com.pragma.api.auth;

import co.com.pragma.api.RouterRest;
import co.com.pragma.api.config.TestSecurityConfig;
import co.com.pragma.api.dto.LoginRequestDTO;
import co.com.pragma.usecase.dto.TokenResponseDTO;
import co.com.pragma.usecase.login.LoginUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest
@Import({
        co.com.pragma.api.config.AuthHandlerTestConfig.class,
        co.com.pragma.api.config.TestSecurityConfig.class
})
class AuthHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LoginUseCase loginUseCase;

    @Configuration
    static class AuthHandlerTestConfig {

        @Bean
        public LoginUseCase loginUseCase() {
            return Mockito.mock(LoginUseCase.class);
        }

        @Bean
        public Validator validator() {
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .correoElectronico("leidy@example.com")
                .contrasena("claveSegura123")
                .build();

        TokenResponseDTO response = TokenResponseDTO.builder()
                .token("jwt-token-generado")
                .tipo("Bearer")
                .expiracion(System.currentTimeMillis() + 3600000)
                .build();

        Mockito.when(loginUseCase.login(request.getCorreoElectronico(), request.getContrasena()))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponseDTO.class)
                .value(token -> {
                    assert token.getToken().equals("jwt-token-generado");
                    assert token.getTipo().equals("Bearer");
                });
    }
}
*/
