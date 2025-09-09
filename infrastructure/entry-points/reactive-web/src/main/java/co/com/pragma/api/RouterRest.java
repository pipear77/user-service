package co.com.pragma.api;

import co.com.pragma.api.auth.AuthHandler;
import co.com.pragma.api.dto.*;
import co.com.pragma.usecase.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@Tag(name = "Usuarios", description = "Operaciones para registrar, autenticar y validar usuarios")
public class RouterRest {

    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "loginUsuario",
                            summary = "Autenticación de usuario",
                            description = "Genera un token JWT si las credenciales son válidas",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Token generado exitosamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = TokenResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/validate",
                    method = RequestMethod.GET,
                    beanClass = AuthHandler.class,
                    beanMethod = "validateToken",
                    operation = @Operation(
                            operationId = "validateToken",
                            summary = "Validar token JWT",
                            description = "Retorna los datos del usuario autenticado si el token es válido",
                            tags = {"Usuarios"},
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Token válido",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UsuarioAutenticadoDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "saveUsuario",
                            summary = "Registrar usuario",
                            description = "Registra un nuevo usuario en el sistema",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UsuarioResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAllUsuarios",
                            summary = "Listar usuarios",
                            description = "Obtiene todos los usuarios registrados",
                            tags = {"Usuarios"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de usuarios",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UsuarioResponseDTO.class)
                                            )
                                    )
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/usuarios/{documento}",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getByDocumento",
                    operation = @Operation(
                            operationId = "getUsuarioPorDocumento",
                            summary = "Buscar usuario por documento",
                            description = "Obtiene un usuario específico por número de documento",
                            tags = {"Usuarios"},
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UsuarioResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> usuarioRoutes(Handler handler, AuthHandler authHandler) {
        return RouterFunctions.route(POST("/api/v1/login"), authHandler::login)
                .andRoute(GET("/api/v1/validate"), authHandler::validateToken)
                .andRoute(POST("/api/v1/usuarios"), handler::save)
                .andRoute(GET("/api/v1/usuarios/{documento}"), handler::getByDocumento)
                .andRoute(GET("/api/v1/usuarios"), handler::getAll);
    }
}
