package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUsuarioRequestMapper {

    @Mapping(target = "id", ignore = true) // El ID se genera en el use case
    Usuario toUsuario(UsuarioRequestDTO dto);
}
