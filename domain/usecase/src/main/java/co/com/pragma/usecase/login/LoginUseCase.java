package co.com.pragma.usecase.login;

import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import co.com.pragma.usecase.dto.TokenResponseDTO;
import co.com.pragma.usecase.exceptions.CredencialesInvalidasException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderRepository passwordEncoder;
    private final JwtProviderRepository jwtProvider;

    public Mono<TokenResponseDTO> login(String correo, String contrasena) {
        return usuarioRepository.findByCorreoElectronico(correo)
                .switchIfEmpty(Mono.error(new CredencialesInvalidasException("Usuario no encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
                        return Mono.error(new CredencialesInvalidasException("Contraseña no configurada"));
                    }

                    if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                        return Mono.error(new CredencialesInvalidasException("Credenciales inválidas"));
                    }

                    return Mono.just(TokenResponseDTO.builder()
                            .token(jwtProvider.generateToken(usuario))
                            .tipo("Bearer")
                            .expiracion(jwtProvider.getExpirationTimestamp())
                            .build());
                });
    }
}
