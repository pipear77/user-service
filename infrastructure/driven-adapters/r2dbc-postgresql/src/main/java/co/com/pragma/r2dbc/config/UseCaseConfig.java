package co.com.pragma.r2dbc.config;

import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.login.LoginUseCase;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoderRepository passwordEncoderRepository
    ) {
        return new RegistrarUsuarioUseCase(usuarioRepository, passwordEncoderRepository);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoderRepository passwordEncoderRepository,
            JwtProviderRepository jwtProviderRepository
    ) {
        return new LoginUseCase(usuarioRepository, passwordEncoderRepository, jwtProviderRepository);
    }
}

