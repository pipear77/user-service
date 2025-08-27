package co.com.pragma.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.springframework.context.annotation.Bean;


@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
        @Bean
        public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
                return new RegistrarUsuarioUseCase(usuarioRepository);
        }

}
