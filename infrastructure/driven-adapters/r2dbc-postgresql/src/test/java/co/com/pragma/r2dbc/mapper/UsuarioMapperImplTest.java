package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;


class UsuarioMapperImplTest {
    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    void shouldMapToEntity() {
        Usuario usuario = Usuario.builder()
                .id("1")
                .nombres("Juan")
                .correoElectronico("juan@correo.com")
                .build();

        UsuarioEntity entity = mapper.toEntity(usuario);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo("1");
        assertThat(entity.getNombres()).isEqualTo("Juan");
        assertThat(entity.getCorreoElectronico()).isEqualTo("juan@correo.com");
    }

    @Test
    void shouldMapToDomain() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId("1");
        entity.setNombres("Juan");
        entity.setCorreoElectronico("juan@correo.com");

        Usuario usuario = mapper.toDomain(entity);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo("1");
        assertThat(usuario.getNombres()).isEqualTo("Juan");
        assertThat(usuario.getCorreoElectronico()).isEqualTo("juan@correo.com");
    }

}