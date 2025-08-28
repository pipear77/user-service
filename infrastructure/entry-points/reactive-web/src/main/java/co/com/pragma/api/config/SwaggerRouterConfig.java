package co.com.pragma.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class SwaggerRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> swaggerUiRouter() {
        return RouterFunctions.resources("/swagger-ui/**", new org.springframework.core.io.ClassPathResource("META-INF/resources/webjars/swagger-ui/"));
    }
}