package co.com.pragma.r2dbc.common.exceptions;


import static co.com.pragma.r2dbc.common.Constantes.ROL_NO_ENCONTRADO;

public class RolNoEncontradoException extends RuntimeException{
    public RolNoEncontradoException() {
        super(ROL_NO_ENCONTRADO);
    }

    public RolNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
