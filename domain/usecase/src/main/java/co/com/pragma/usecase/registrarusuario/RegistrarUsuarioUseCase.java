package co.com.pragma.usecase.registrarusuario;
import reactor.core.publisher.Mono;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.usecase.common.UsuarioValidationPipeline;
import co.com.pragma.usecase.common.validacion.fields.*;
import co.com.pragma.usecase.exceptions.CorreoYaRegistradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_CORREO_DUPLICADO;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase implements RegistrarUsuarioUseCaseInterface {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderRepository passwordEncoderRepository;

    @Override
    public Mono<Usuario> save(Usuario usuario) {
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
                                    .id(UUID.randomUUID().toString()) // ← genera el UUID aquí
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
