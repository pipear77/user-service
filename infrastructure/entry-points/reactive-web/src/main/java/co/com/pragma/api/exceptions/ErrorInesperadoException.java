package co.com.pragma.api.exceptions;


import static co.com.pragma.r2dbc.common.Constantes.DESCRIPCION_ERROR_INESPERADO;

public class ErrorInesperadoException extends RuntimeException {
    public ErrorInesperadoException() {
        super(DESCRIPCION_ERROR_INESPERADO);
    }

    public ErrorInesperadoException(String mensaje) {
        super(mensaje);
    }
}
