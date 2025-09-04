package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toDomain(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario usuario);
}
