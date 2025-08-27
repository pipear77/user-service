package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.exceptions.CampoObligatorioException;
import co.com.pragma.usecase.exceptions.CorreoYaRegistradoException;
import co.com.pragma.usecase.exceptions.FormatoCorreoInvalidoException;
import co.com.pragma.usecase.exceptions.SalarioFueraDeRangoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class RegistrarUsuarioUseCase implements RegistrarUsuarioUseCaseInterface {

    private final UsuarioRepository usuarioRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        return validarUsuario(usuario)
                .then(Mono.defer(() ->
                        usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                                .flatMap(existing -> Mono.<Usuario>error(
                                        new CorreoYaRegistradoException(usuario.getCorreoElectronico())))
                                .switchIfEmpty(Mono.defer(() -> usuarioRepository.save(usuario)))
                ));
    }




    Mono<Void> validarUsuario(Usuario usuario) {
        if (isNullOrEmpty(usuario.getNombres())) {
            return Mono.error(new CampoObligatorioException("nombres"));
        }
        if (isNullOrEmpty(usuario.getApellidos())) {
            return Mono.error(new CampoObligatorioException("apellidos"));
        }
        if (isNullOrEmpty(usuario.getDireccion())) {
            return Mono.error(new CampoObligatorioException("direccion"));
        }
        if (isNullOrEmpty(usuario.getTelefono())) {
            return Mono.error(new CampoObligatorioException("telefono"));
        }
        if (isNullOrEmpty(usuario.getCorreoElectronico())) {
            return Mono.error(new CampoObligatorioException("correo electrónico"));
        }
        if (!EMAIL_PATTERN.matcher(usuario.getCorreoElectronico()).matches()) {
            return Mono.error(new FormatoCorreoInvalidoException());
        }
        if (usuario.getSalarioBase().compareTo(SALARIO_MINIMO) < 0 ||
                usuario.getSalarioBase().compareTo(SALARIO_MAXIMO) > 0) {
            return Mono.error(new SalarioFueraDeRangoException(SALARIO_MINIMO, SALARIO_MAXIMO));
        }

        return Mono.empty();
    }


    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public Flux<Usuario> getAllUsuarios() {
        return usuarioRepository.findAllUsuarios();
    }
}
