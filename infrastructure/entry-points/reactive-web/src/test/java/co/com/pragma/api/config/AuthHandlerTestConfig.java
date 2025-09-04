package co.com.pragma.api.config;

import co.com.pragma.api.Handler;
import co.com.pragma.api.auth.AuthHandler;
import co.com.pragma.usecase.login.LoginUseCase;
import jakarta.validation.Validator;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class AuthHandlerTestConfig {

    @Bean
    public LoginUseCase loginUseCase() {
        return Mockito.mock(LoginUseCase.class);
    }

    @Bean
    public AuthHandler authHandler(LoginUseCase loginUseCase) {
        return new AuthHandler(loginUseCase, validator());
    }

    @Bean
    public jakarta.validation.Validator validator() {
        return jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Bean
    public Handler handler() {
        return Mockito.mock(Handler.class);
    }

    @Bean
    public RouterFunction<ServerResponse> loginRoute(AuthHandler authHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/api/v1/login"), authHandler::login);
    }



    @Bean
    public AuthHandler authHandler(LoginUseCase loginUseCase, Validator validator) {
        return new AuthHandler(loginUseCase, validator);
    }

}