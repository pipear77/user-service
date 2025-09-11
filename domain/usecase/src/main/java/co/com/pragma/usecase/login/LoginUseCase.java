package co.com.pragma.usecase.login;

import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import co.com.pragma.usecase.common.ConstantesUsuario;
import co.com.pragma.usecase.dto.TokenResponseDTO;
import co.com.pragma.usecase.exceptions.CredencialesInvalidasException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderRepository passwordEncoder;
    private final JwtProviderRepository jwtProvider;
    private final RolRepository rolRepository;

    public Mono<TokenResponseDTO> login(String correo, String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return Mono.error(new CredencialesInvalidasException(ConstantesUsuario.ERROR_CONTRASENA_REQUERIDA));
        }
        return usuarioRepository.findByCorreo(correo)
                .flatMap(usuario -> {
                    if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                        return Mono.error(new CredencialesInvalidasException());
                    }

                    UUID idRol = usuario.getIdRol();
                    return rolRepository.findById(idRol)
                            .switchIfEmpty(Mono.error(new CredencialesInvalidasException()))
                            .map(rol -> {
                                Map<String, Object> claims = new HashMap<>();
                                claims.put("id", usuario.getId().toString());
                                claims.put("rol", rol.getName()); // nombre del rol
                                claims.put("documento", usuario.getNumeroDocumento());
                                claims.put("nombres", usuario.getNombres());
                                claims.put("apellidos", usuario.getApellidos());
                                claims.put("salarioBase", usuario.getSalarioBase().toPlainString()); // como String

                                return TokenResponseDTO.builder()
                                        .token(jwtProvider.generateToken(usuario.getCorreo(), claims))
                                        .tipo("Bearer")
                                        .expiracion(jwtProvider.getExpirationTimestamp())
                                        .build();
                            });
                });
    }

}
