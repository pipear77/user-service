package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.exceptions.CampoObligatorioException;
import co.com.pragma.usecase.exceptions.CorreoYaRegistradoException;
import co.com.pragma.usecase.exceptions.FormatoCorreoInvalidoException;
import co.com.pragma.usecase.exceptions.SalarioFueraDeRangoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private RegistrarUsuarioUseCase useCase;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarUsuarioUseCase(usuarioRepository);

        usuarioValido = Usuario.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .telefono("3111234567")
                .correoElectronico("juan@mail.com")
                .salarioBase(new BigDecimal("3000000"))
                .build();
    }

    // ✅ Casos exitosos

    @Test
    void debeRegistrarUsuarioExitosamente() {
        when(usuarioRepository.findByCorreoElectronico(usuarioValido.getCorreoElectronico())).thenReturn(Mono.empty());
        when(usuarioRepository.save(usuarioValido)).thenReturn(Mono.just(usuarioValido));

        StepVerifier.create(useCase.save(usuarioValido))
                .expectNext(usuarioValido)
                .verifyComplete();
    }

    @Test
    void noDebeFallarSiTodosLosCamposSonValidos() {
        StepVerifier.create(useCase.validarUsuario(usuarioValido))
                .verifyComplete();
    }

    @Test
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
                .verifyComplete();
    }


    // ❌ Casos de error por validación

    @Test
    void debeFallarSiNombresEsNulo() {
        Usuario usuario = usuarioValido.toBuilder().nombres(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("nombres"))
                .verify();
    }

    @Test
    void debeFallarSiApellidosEsNulo() {
        Usuario usuario = usuarioValido.toBuilder().apellidos(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("apellidos"))
                .verify();
    }

    @Test
    void debeFallarSiDireccionEsNula() {
        Usuario usuario = usuarioValido.toBuilder().direccion(null).build();

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(e -> e instanceof CampoObligatorioException &&
                        ((CampoObligatorioException) e).getCampo().equals("direccion"))
                .verify();
    }

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
    }
}