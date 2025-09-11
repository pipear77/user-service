package co.com.pragma.usecase.exceptions;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_FORMATO_CORREO;

public class FormatoCorreoInvalidoException extends RuntimeException {
    public FormatoCorreoInvalidoException() {
        super(ERROR_FORMATO_CORREO);
    }
}
