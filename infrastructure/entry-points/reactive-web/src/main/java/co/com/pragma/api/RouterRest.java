package co.com.pragma.api;

import co.com.pragma.model.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Operaciones para registrar y consultar usuarios")
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            summary = "Registrar usuario",
                            description = "Registra un nuevo usuario en el sistema"
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            summary = "Listar usuarios",
                            description = "Obtiene todos los usuarios registrados"
                    )
            )
    })
    public RouterFunction<ServerResponse> usuarioRoutes(Handler handler) {
        return RouterFunctions.route(POST("/api/v1/usuarios"), handler::save)
                .andRoute(GET("/api/v1/usuarios"), handler::getAll);
    }
}