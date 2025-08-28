// src/main/java/co/com/pragma/api/RouterRest.java

package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@Tag(name = "Usuarios", description = "Operaciones para registrar y consultar usuarios")
public class RouterRest {

    @Bean
    @RouterOperations({ // 👈 Mueve esta anotación aquí
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            summary = "Registrar usuario",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            summary = "Listar usuarios",
                            description = "Obtiene todos los usuarios registrados",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de usuarios",
                                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> usuarioRoutes(Handler handler) {
        return RouterFunctions.route(POST("/api/v1/usuarios"), handler::save)
                .andRoute(GET("/api/v1/usuarios"), handler::getAll);
    }
}