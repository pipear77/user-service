package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioRequestDTO;
import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface IUsuarioRequestMapper {

    @Mapping(target = "id", ignore = true) // El ID se genera en el use case
    @Mapping(source = "idRol", target = "idRol", qualifiedByName = "stringToUuid")
    Usuario toUsuario(UsuarioRequestDTO dto);

    @Named("stringToUuid")
    static UUID stringToUuid(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
