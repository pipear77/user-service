package co.com.pragma.r2dbc.common.exceptions;

import co.com.pragma.r2dbc.common.Constantes;

import static co.com.pragma.r2dbc.common.Constantes.USUARIO_NO_ENCONTRADO;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super(USUARIO_NO_ENCONTRADO);
    }

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
