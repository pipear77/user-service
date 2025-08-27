package co.com.pragma.api.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa - API de Usuarios")
                        .version("1.0")
                        .description("Documentación de endpoints funcionales para registrar y consultar usuarios"));
    }

    @Bean
    public OpenApiCustomiser usuarioApiCustomiser() {
        return openApi -> {
            Paths paths = new Paths();

            // POST /api/v1/usuarios
            Operation postOperation = new Operation()
                    .summary("Registrar nuevo usuario")
                    .description("Valida y guarda un usuario si el correo no está registrado")
                    .requestBody(new RequestBody()
                            .description("Objeto Usuario")
                            .required(true)
                            .content(OpenApiSchemas.usuarioRequest()))
                    .responses(new ApiResponses()
                            .addApiResponse("201", new ApiResponse().description("Usuario creado"))
                            .addApiResponse("400", new ApiResponse().description("Error de validación"))
                            .addApiResponse("409", new ApiResponse().description("Correo ya registrado")));

            paths.addPathItem("/api/v1/usuarios", new PathItem().post(postOperation));

            // GET /api/v1/usuarios
            Operation getOperation = new Operation()
                    .summary("Obtener todos los usuarios")
                    .description("Retorna la lista completa de usuarios registrados")
                    .responses(new ApiResponses()
                            .addApiResponse("200", new ApiResponse().description("Lista de usuarios")));

            paths.get("/api/v1/usuarios").get(getOperation);

            openApi.paths(paths);
        };
    }


}
