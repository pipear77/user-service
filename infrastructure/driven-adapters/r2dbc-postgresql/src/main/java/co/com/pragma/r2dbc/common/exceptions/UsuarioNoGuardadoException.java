package co.com.pragma.r2dbc.common.exceptions;

import static co.com.pragma.r2dbc.common.Constantes.USUARIO_NO_GUARDADO;

public class UsuarioNoGuardadoException extends RuntimeException {
    public UsuarioNoGuardadoException() {
        super(USUARIO_NO_GUARDADO);
    }

    public UsuarioNoGuardadoException(String mensaje) {
        super(mensaje);
    }
}
