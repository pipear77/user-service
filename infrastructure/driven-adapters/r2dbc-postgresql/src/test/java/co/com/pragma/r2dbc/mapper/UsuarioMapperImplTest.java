package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioMapperImplTest {

    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    void shouldMapToEntity() {
        UUID uuid = UUID.randomUUID();

        Usuario usuario = Usuario.builder()
                .id(uuid)
                .nombres("Juan")
                .correoElectronico("juan@correo.com")
                .build();

        UsuarioEntity entity = mapper.toEntity(usuario);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(uuid.toString());
        assertThat(entity.getNombres()).isEqualTo("Juan");
        assertThat(entity.getCorreoElectronico()).isEqualTo("juan@correo.com");
    }

    @Test
    void shouldMapToDomain() {
        UUID uuid = UUID.randomUUID();

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(uuid.toString());
        entity.setNombres("Juan");
        entity.setCorreoElectronico("juan@correo.com");

        Usuario usuario = mapper.toDomain(entity);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(uuid);
        assertThat(usuario.getNombres()).isEqualTo("Juan");
        assertThat(usuario.getCorreoElectronico()).isEqualTo("juan@correo.com");
    }
}
