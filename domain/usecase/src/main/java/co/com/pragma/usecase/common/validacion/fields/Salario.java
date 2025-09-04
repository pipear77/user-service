package co.com.pragma.usecase.common.validacion.fields;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.common.validacion.UsuarioValidacion;
import co.com.pragma.usecase.exceptions.CampoObligatorioException;
import co.com.pragma.usecase.exceptions.error.CodigosEstadoHttp;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static co.com.pragma.usecase.common.ConstantesUsuario.*;

public class Salario implements UsuarioValidacion{
    private static final BigDecimal MAX_SALARIO = BigDecimal.valueOf(15000000);
    @Override
    public Mono<Void> validar(Usuario usuario) {
        if (usuario.getSalarioBase() == null || usuario.getSalarioBase().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new CampoObligatorioException(ERROR_SALARIO_REQUERIDO, CodigosEstadoHttp.BAD_REQUEST.getCodigo()));
        }

        if (usuario.getSalarioBase().compareTo(MAX_SALARIO) >= 0) {
            return Mono.error(new CampoObligatorioException(ERROR_SALARIO_NUMERICO, CodigosEstadoHttp.BAD_REQUEST.getCodigo()));
        }


        return Mono.empty();
    }

}
