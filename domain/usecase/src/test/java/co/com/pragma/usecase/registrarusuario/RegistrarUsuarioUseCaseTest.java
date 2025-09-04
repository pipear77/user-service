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
import java.time.LocalDate;

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
                .idRol("ROL-ADMIN")
                .build();
    }

    @Test
    void save_debeGuardarUsuario_siCorreoNoExiste() {
        Usuario usuario = usuarioValido("nuevo@correo.com");

        when(usuarioRepository.existsByEmail(usuario.getCorreoElectronico())).thenReturn(Mono.just(false));
        when(passwordEncoderRepository.encode(usuario.getNumeroDocumento())).thenReturn("encoded123");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(usuarioRepository.save(any())).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            return Mono.just(u);
        });

        StepVerifier.create(registrarUsuarioUseCase.save(usuario))
                .expectNextMatches(saved -> {
                    assertNotNull(saved.getId(), "El ID no debe ser nulo");
                    assertDoesNotThrow(() -> UUID.fromString(saved.getId()), "El ID debe tener formato UUID");
                    assertEquals("encoded123", saved.getContrasena());
                    return true;
                })
                .verifyComplete();

        verify(usuarioRepository).existsByEmail(usuario.getCorreoElectronico());
        verify(passwordEncoderRepository).encode(usuario.getNumeroDocumento());
        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioGuardado = captor.getValue();
        assertNotNull(usuarioGuardado.getId());
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
                .idRol("ROL-ADMIN")
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

    @Test
    void existsByDocumentNumber_delegaEnRepositorio_true() {
        String documento = "123456789";
        when(usuarioRepository.existsByDocumentNumber(documento)).thenReturn(Mono.just(true));

        StepVerifier.create(registrarUsuarioUseCase.existsByDocumentNumber(documento))
                .expectNext(true)
                .verifyComplete();

        verify(usuarioRepository).existsByDocumentNumber(documento);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void existsByDocumentNumber_delegaEnRepositorio_false() {
        String documento = "987654321";
        when(usuarioRepository.existsByDocumentNumber(documento)).thenReturn(Mono.just(false));

        StepVerifier.create(registrarUsuarioUseCase.existsByDocumentNumber(documento))
                .expectNext(false)
                .verifyComplete();

        verify(usuarioRepository).existsByDocumentNumber(documento);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void existsByDocumentNumber_debePropagarErrorDelRepositorio() {
        String documento = "error";
        RuntimeException boom = new RuntimeException("fallo en repo");
        when(usuarioRepository.existsByDocumentNumber(documento)).thenReturn(Mono.error(boom));

        StepVerifier.create(registrarUsuarioUseCase.existsByDocumentNumber(documento))
                .expectErrorMatches(error -> error == boom)
                .verify();

        verify(usuarioRepository).existsByDocumentNumber(documento);
        verifyNoMoreInteractions(usuarioRepository);
    }
}