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
import java.util.UUID;

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
                .idRol(UUID.randomUUID()) // ✅ UUID real
                .build();
    }

    @Test
    void save_debeGuardarUsuario_siCorreoNoExiste() {
        // Simula el ID generado por la base
        UUID idGenerado = UUID.randomUUID();

        // Usuario de entrada sin ID
        Usuario usuario = usuarioValido("nuevo@correo.com");

        // Simula que el correo no está registrado
        when(usuarioRepository.existsByEmail(usuario.getCorreoElectronico()))
                .thenReturn(Mono.just(false));

        // Simula la codificación de la contraseña
        when(passwordEncoderRepository.encode(usuario.getNumeroDocumento()))
                .thenReturn("encoded123");

        // Captura el usuario que se envía al repositorio
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        // Simula el repositorio devolviendo el usuario con ID asignado
        when(usuarioRepository.save(any()))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    return Mono.just(u.toBuilder()
                            .id(idGenerado)
                            .contrasena("encoded123")
                            .build());
                });

        // Verifica el resultado del use case
        StepVerifier.create(registrarUsuarioUseCase.save(usuario))
                .assertNext(saved -> {
                    assertNotNull(saved.getId(), "El ID no debe ser nulo");
                    assertEquals(idGenerado, saved.getId(), "El ID debe coincidir con el generado");
                    assertEquals("encoded123", saved.getContrasena());
                })
                .verifyComplete();

        // Verifica que se haya llamado correctamente al repositorio
        verify(usuarioRepository).existsByEmail(usuario.getCorreoElectronico());
        verify(passwordEncoderRepository).encode(usuario.getNumeroDocumento());
        verify(usuarioRepository).save(captor.capture());

        // Verifica el usuario capturado antes del save
        Usuario usuarioGuardado = captor.getValue();
        assertNull(usuarioGuardado.getId(), "El ID debe ser null antes del save");
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
                .idRol(UUID.randomUUID()) // ✅ UUID real
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
<<<<<<< HEAD
    void debeRetornarTodosLosUsuarios() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        Usuario usuario1 = usuarioValido.toBuilder().id(uuid1).build();
        Usuario usuario2 = usuarioValido.toBuilder()
                .id(uuid2)
                .correoElectronico("otro@mail.com")
                .build();

        when(usuarioRepository.findAllUsuarios()).thenReturn(Flux.just(usuario1, usuario2));

        StepVerifier.create(useCase.getAllUsuarios())
                .expectNextMatches(u -> u.getId().equals(uuid1))
                .expectNextMatches(u -> u.getId().equals(uuid2) && u.getCorreoElectronico().equals("otro@mail.com"))
=======
    void existsByDocumentNumber_delegaEnRepositorio_true() {
        String documento = "123456789";
        when(usuarioRepository.existsByDocumentNumber(documento)).thenReturn(Mono.just(true));

        StepVerifier.create(registrarUsuarioUseCase.existsByDocumentNumber(documento))
                .expectNext(true)
>>>>>>> develop
                .verifyComplete();

<<<<<<< HEAD

    // ❌ Casos de error por validación

    @Test
    void debeFallarSiNombresEsNulo() {
        Usuario usuario = usuarioValido.toBuilder().nombres(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("nombres"))
                .verify();
=======
        verify(usuarioRepository).existsByDocumentNumber(documento);
        verifyNoMoreInteractions(usuarioRepository);
>>>>>>> develop
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

<<<<<<< HEAD
    @Test
    void debeFallarSiTelefonoEsNulo() {
        Usuario usuario = usuarioValido.toBuilder().telefono(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("telefono"))
                .verify();
    }

    @Test
    void debeFallarSiCorreoEsNulo() {
        Usuario usuario = usuarioValido.toBuilder().correoElectronico(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("correo electronico"))
                .verify();
    }

    @Test
    void debeFallarSiCorreoEsInvalido() {
        Usuario usuario = usuarioValido.toBuilder().correoElectronico("correo-invalido").build();

        StepVerifier.create(useCase.save(usuario))
                .expectError(FormatoCorreoInvalidoException.class)
                .verify();
    }

    @Test
    void debeFallarSiSalarioEsMayorAlPermitido() {
        Usuario usuario = usuarioValido.toBuilder().salarioBase(new BigDecimal("20000000")).build();

        StepVerifier.create(useCase.save(usuario))
                .expectError(SalarioFueraDeRangoException.class)
                .verify();
    }

    @Test
    void debeFallarSiCorreoYaExiste() {
        when(usuarioRepository.findByCorreoElectronico(usuarioValido.getCorreoElectronico()))
                .thenReturn(Mono.just(usuarioValido));

        StepVerifier.create(useCase.save(usuarioValido))
                .expectErrorMatches(e -> e instanceof CorreoYaRegistradoException &&
                        e.getMessage().contains(usuarioValido.getCorreoElectronico()))
                .verify();
=======
        verify(usuarioRepository).existsByDocumentNumber(documento);
        verifyNoMoreInteractions(usuarioRepository);
>>>>>>> develop
    }

}