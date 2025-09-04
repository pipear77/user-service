package co.com.pragma.usecase.registrarusuario;
import co.com.pragma.model.usuario.gateways.JwtProviderRepository;
import lombok.Getter;
import reactor.core.publisher.Mono;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.usecase.common.UsuarioValidationPipeline;
import co.com.pragma.usecase.common.validacion.fields.*;
import co.com.pragma.usecase.exceptions.CorreoYaRegistradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.UUID;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_CORREO_DUPLICADO;

@Getter
@RequiredArgsConstructor
public class RegistrarUsuarioUseCase implements RegistrarUsuarioUseCaseInterface {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderRepository passwordEncoderRepository;
    private final JwtProviderRepository jwtProvider;

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        return validarUsuario(usuario)
                .then(Mono.defer(() ->
                        usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                                .flatMap(existing -> Mono.<Usuario>error(
                                        new CorreoYaRegistradoException(usuario.getCorreoElectronico())))
                                .switchIfEmpty(Mono.defer(() -> {
                                    usuario.setId(UUID.fromString(UUID.randomUUID().toString()));
                                    return usuarioRepository.save(usuario);
                                }))
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
            return Mono.error(new CampoObligatorioException("correo electronico"));
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
        UsuarioValidationPipeline pipeline = new UsuarioValidationPipeline()
                .agregarValidacion(new Nombre())
                .agregarValidacion(new Apellido())
                .agregarValidacion(new NumeroDocumento())
                .agregarValidacion(new CorreoElectronico())
                .agregarValidacion(new Salario());

        return pipeline.validar(usuario)
                .then(usuarioRepository.existsByEmail(usuario.getCorreoElectronico()))
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new CorreoYaRegistradoException(ERROR_CORREO_DUPLICADO + usuario.getCorreoElectronico()));
                    }
                    return Mono.just(usuario)
                            .map(u -> u.toBuilder()
                                    .id(null)
                                    .contrasena(passwordEncoderRepository.encode(u.getNumeroDocumento()))
                                    .build())
                            .flatMap(usuarioRepository::save);
                });
    }

    @Override
    public Flux<Usuario> getAllUsuarios() {
        return usuarioRepository.findAllUsuarios();
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsByDocumentNumber(String documentNumber) {
        return usuarioRepository.existsByDocumentNumber(documentNumber);
    }

}
