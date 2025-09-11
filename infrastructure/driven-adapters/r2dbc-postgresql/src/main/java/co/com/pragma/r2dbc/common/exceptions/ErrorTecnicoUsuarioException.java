package co.com.pragma.r2dbc.common.exceptions;

import static co.com.pragma.r2dbc.common.Constantes.USUARIO_NO_GUARDADO;

public class ErrorTecnicoUsuarioException extends RuntimeException {
    public ErrorTecnicoUsuarioException() {
        super(USUARIO_NO_GUARDADO);
    }

    public ErrorTecnicoUsuarioException(String mensaje) {
        super(mensaje);
    }
}
