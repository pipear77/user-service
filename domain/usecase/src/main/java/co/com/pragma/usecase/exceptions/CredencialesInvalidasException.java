package co.com.pragma.usecase.exceptions;

import co.com.pragma.usecase.common.ConstantesUsuario;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super(ConstantesUsuario.CREDENCIALES_INVALIDAS);
    }

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
