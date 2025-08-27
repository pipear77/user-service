package co.com.pragma.api.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.NumberSchema;



public class OpenApiSchemas {
    public static Content usuarioRequest() {
        ObjectSchema schema = (ObjectSchema) new ObjectSchema()
                .title("Usuario")
                .description("Objeto que representa un usuario")
                .addProperties("nombres", new StringSchema().description("Nombres del usuario"))
                .addProperties("apellidos", new StringSchema().description("Apellidos del usuario"))
                .addProperties("fechaNacimiento", new StringSchema().format("date").description("Fecha de nacimiento"))
                .addProperties("direccion", new StringSchema().description("Dirección del usuario"))
                .addProperties("telefono", new StringSchema().description("Teléfono de contacto"))
                .addProperties("correoElectronico", new StringSchema().format("email").description("Correo electrónico"))
                .addProperties("salarioBase", new NumberSchema().format("double").description("Salario base"));

        return new Content().addMediaType("application/json", new MediaType().schema(schema));
    }

}

