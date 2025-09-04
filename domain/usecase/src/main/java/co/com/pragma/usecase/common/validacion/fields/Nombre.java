package co.com.pragma.usecase.common.validacion.fields;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.common.validacion.UsuarioValidacion;
import co.com.pragma.usecase.exceptions.CampoObligatorioException;
import co.com.pragma.usecase.exceptions.error.CodigosEstadoHttp;
import reactor.core.publisher.Mono;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_DOCUMENTO_REQUERIDO;
import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_NOMBRE_REQUERIDO;

public class Nombre implements UsuarioValidacion{
    @Override
    public Mono<Void> validar(Usuario usuario) {
        if (usuario.getNombres() == null || usuario.getNombres().isEmpty()) {
            return Mono.error(new CampoObligatorioException("nombres", CodigosEstadoHttp.BAD_REQUEST.getCodigo()));
        }

        return Mono.empty();
    }

}
