package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;
import co.com.pragma.r2dbc.entity.UsuarioEntity;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioEntity toEntity(Usuario usuario);
    Usuario toDomain(UsuarioEntity entity);
}
