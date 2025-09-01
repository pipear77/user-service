package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private Handler handler;

    @Test
    void testPOSTUsuarioRoute() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(); // rellena si es necesario
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();

        // El handler devuelve directamente un Mono<ServerResponse>
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
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();

        Mockito.when(handler.getAll(any()))
                .thenReturn(ServerResponse.ok().bodyValue(responseDTO));

        webTestClient.get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk();
    }
}