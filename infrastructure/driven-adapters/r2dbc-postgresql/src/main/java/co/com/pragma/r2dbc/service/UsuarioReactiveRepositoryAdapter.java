package co.com.pragma.r2dbc.service;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,               // Dominio
        UsuarioEntity,         // Entidad adaptador
        String,                // Tipo de ID
        UsuarioReactiveRepository // Repositorio adaptador
        > {

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }
}