package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioResponseDTO;
import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUsuarioResponseMapper {

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);
}
