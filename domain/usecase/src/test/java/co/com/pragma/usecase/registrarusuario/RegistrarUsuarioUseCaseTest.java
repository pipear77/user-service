package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.model.usuario.gateways.PasswordEncoderRepository;
import co.com.pragma.usecase.common.UsuarioValidationPipeline;
import co.com.pragma.usecase.common.validacion.fields.Nombre;
import co.com.pragma.usecase.exceptions.CorreoYaRegistradoException;
import co.com.pragma.usecase.exceptions.CampoObligatorioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import java.util.UUID;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_CORREO_DUPLICADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @InjectMocks
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    private static Usuario usuarioValido(String correo) {
        return Usuario.builder()
                .nombres("Leidy")
                .apellidos("Prueba")
                .numeroDocumento("123456789")
                .fechaNacimiento(null)
                .direccion("Calle 123")
                .telefono("3001234567")
                .correoElectronico(correo)
                .contrasena("123456")
                .salarioBase(new BigDecimal("1000000"))
                .idRol(UUID.randomUUID()) //UUID real
                .build();
    }

    @Test
    void save_debeGuardarUsuario_siCorreoNoExiste() {
        UUID idGenerado = UUID.randomUUID();
        Usuario usuario = usuarioValido("nuevo@correo.com");

        when(usuarioRepository.existsByEmail(usuario.getCorreoElectronico()))
                .thenReturn(Mono.just(false));

        //Corrección aquí: codificamos la contraseña, no el documento
        when(passwordEncoderRepository.encode(usuario.getContrasena()))
                .thenReturn("encoded123");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        when(usuarioRepository.save(any()))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    return Mono.just(u.toBuilder()
                            .id(idGenerado)
                            .contrasena("encoded123")
                            .build());
                });

        StepVerifier.create(registrarUsuarioUseCase.save(usuario))
                .assertNext(saved -> {
                    assertNotNull(saved.getId());
                    assertEquals(idGenerado, saved.getId());
                    assertEquals("encoded123", saved.getContrasena());
                })
                .verifyComplete();

        verify(usuarioRepository).existsByEmail(usuario.getCorreoElectronico());
        verify(passwordEncoderRepository).encode(usuario.getContrasena()); // ✅ aquí también
        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioGuardado = captor.getValue();
        assertNull(usuarioGuardado.getId());
        assertEquals("encoded123", usuarioGuardado.getContrasena());
        verifyNoMoreInteractions(usuarioRepository);
    }



    @Test
    void save_debeFallar_siCorreoYaExiste() {
        Usuario usuario = usuarioValido("duplicado@correo.com");

        when(usuarioRepository.existsByEmail(usuario.getCorreoElectronico())).thenReturn(Mono.just(true));

        StepVerifier.create(registrarUsuarioUseCase.save(usuario))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CorreoYaRegistradoException.class, error);
                    assertEquals(ERROR_CORREO_DUPLICADO + usuario.getCorreoElectronico(), error.getMessage());
                })
                .verify();

        verify(usuarioRepository).existsByEmail(usuario.getCorreoElectronico());
        verify(usuarioRepository, never()).save(any());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void pipeline_debeFallar_siNombreEsVacio() {
        Usuario usuario = Usuario.builder()
                .nombres("")
                .apellidos("Prueba")
                .numeroDocumento("123456789")
                .direccion("Calle 123")
                .telefono("3001234567")
                .correoElectronico("ok@correo.com")
                .salarioBase(new BigDecimal("1000000"))
                .idRol(UUID.randomUUID()) // UUID real
                .build();

        UsuarioValidationPipeline pipeline = new UsuarioValidationPipeline()
                .agregarValidacion(new Nombre());

        StepVerifier.create(pipeline.validar(usuario))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CampoObligatorioException.class, error);
                    assertEquals("Campo obligatorio: nombres", error.getMessage());
                })
                .verify();
    }

    @Test
    void existsByEmail_delegaEnRepositorio() {
        String correo = "test@correo.com";
        when(usuarioRepository.existsByEmail(correo)).thenReturn(Mono.just(true));

        StepVerifier.create(registrarUsuarioUseCase.existsByEmail(correo))
                .expectNext(true)
                .verifyComplete();

        verify(usuarioRepository).existsByEmail(correo);
        verifyNoMoreInteractions(usuarioRepository);
    }
}