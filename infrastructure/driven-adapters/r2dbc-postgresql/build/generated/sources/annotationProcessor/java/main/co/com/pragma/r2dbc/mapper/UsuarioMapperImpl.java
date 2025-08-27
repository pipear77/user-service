package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T08:54:17-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioEntity toEntity(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioEntity.UsuarioEntityBuilder usuarioEntity = UsuarioEntity.builder();

        usuarioEntity.id( usuario.getId() );
        usuarioEntity.nombres( usuario.getNombres() );
        usuarioEntity.apellidos( usuario.getApellidos() );
        usuarioEntity.fechaNacimiento( usuario.getFechaNacimiento() );
        usuarioEntity.direccion( usuario.getDireccion() );
        usuarioEntity.telefono( usuario.getTelefono() );
        usuarioEntity.correoElectronico( usuario.getCorreoElectronico() );
        usuarioEntity.salarioBase( usuario.getSalarioBase() );

        return usuarioEntity.build();
    }

    @Override
    public Usuario toDomain(UsuarioEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Usuario.UsuarioBuilder usuario = Usuario.builder();

        usuario.id( entity.getId() );
        usuario.nombres( entity.getNombres() );
        usuario.apellidos( entity.getApellidos() );
        usuario.fechaNacimiento( entity.getFechaNacimiento() );
        usuario.direccion( entity.getDireccion() );
        usuario.telefono( entity.getTelefono() );
        usuario.correoElectronico( entity.getCorreoElectronico() );
        usuario.salarioBase( entity.getSalarioBase() );

        return usuario.build();
    }
}
