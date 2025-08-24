package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase implements RegistrarUsuarioUseCaseInterface {
    private final UsuarioRepository usuarioRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    @Override
    public Mono<Usuario> registrarUsuario(Usuario usuario) {
        return validarUsuario(usuario)
                .then(usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()))
                .flatMap(existing -> {
                    if (existing != null) {
                        return Mono.error(new IllegalArgumentException("El correo ya está registrado"));
                    }
                    return usuarioRepository.save(usuario);
                })
                .switchIfEmpty(usuarioRepository.save(usuario));
    }

    private Mono<Void> validarUsuario(Usuario usuario) {
        if (isNullOrEmpty(usuario.getNombres())) {
            return Mono.error(new IllegalArgumentException("El nombre es obligatorio"));
        }
        if (isNullOrEmpty(usuario.getApellidos())) {
            return Mono.error(new IllegalArgumentException("El apellido es obligatorio"));
        }
        if (isNullOrEmpty(usuario.getCorreoElectronico())) {
            return Mono.error(new IllegalArgumentException("El correo electrónico es obligatorio"));
        }
        if (!EMAIL_PATTERN.matcher(usuario.getCorreoElectronico()).matches()) {
            return Mono.error(new IllegalArgumentException("Formato de correo inválido"));
        }
        if (usuario.getSalarioBase() == null) {
            return Mono.error(new IllegalArgumentException("El salario base es obligatorio"));
        }
        BigDecimal salario = usuario.getSalarioBase();
        if (salario.compareTo(BigDecimal.ZERO) < 0 || salario.compareTo(new BigDecimal("150000000")) > 0) {
            return Mono.error(new IllegalArgumentException("El salario debe estar entre 0 y 150.000.000"));
        }

        return Mono.empty();
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }


}
