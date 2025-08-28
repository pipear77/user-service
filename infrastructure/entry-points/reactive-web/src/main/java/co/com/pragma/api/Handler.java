package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.api.dto.UsuarioResponseDTO;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class Handler {

    private final RegistrarUsuarioUseCase useCase;

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(UsuarioRequestDTO.class)
                .doOnSubscribe(sub -> log.info("Iniciando procesamiento de solicitud de creación de usuario"))
                .doOnNext(dto -> log.debug("DTO recibido: {}", dto))
                .map(this::toUsuarioDomain)
                .doOnNext(usuario -> log.debug("Mapeado a dominio: {}", usuario))
                .flatMap(useCase::save)
                .doOnNext(saved -> log.info("Usuario guardado exitosamente: {}", saved.getId()))
                .map(this::toUsuarioResponseDTO)
                .doOnNext(dto -> log.debug("Mapeado a DTO de respuesta: {}", dto))
                .flatMap(usuarioDto -> ServerResponse.status(201).bodyValue(usuarioDto))
                .doOnError(error -> log.error("Error al guardar usuario: {}", error.getMessage(), error))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error: " + e.getMessage()));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return useCase.getAllUsuarios()
                .doOnSubscribe(sub -> log.info("Solicitando todos los usuarios"))
                .doOnNext(usuario -> log.debug("Usuario recuperado: {}", usuario.getId()))
                .map(this::toUsuarioResponseDTO)
                .doOnNext(dto -> log.debug("Mapeado a DTO: {}", dto))
                .collectList()
                .doOnNext(lista -> log.info("Total usuarios recuperados: {}", lista.size()))
                .flatMap(lista -> ServerResponse.ok().bodyValue(lista))
                .doOnError(error -> log.error("Error al recuperar usuarios: {}", error.getMessage(), error));
    }

    private Usuario toUsuarioDomain(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correoElectronico(dto.getCorreoElectronico())
                .salarioBase(dto.getSalarioBase())
                .build();
    }

    private UsuarioResponseDTO toUsuarioResponseDTO(Usuario domain) {
        return UsuarioResponseDTO.builder()
                .id(domain.getId())
                .nombres(domain.getNombres())
                .apellidos(domain.getApellidos())
                .fechaNacimiento(domain.getFechaNacimiento())
                .direccion(domain.getDireccion())
                .telefono(domain.getTelefono())
                .correoElectronico(domain.getCorreoElectronico())
                .salarioBase(domain.getSalarioBase())
                .build();
    }
}