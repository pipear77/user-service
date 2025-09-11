package co.com.pragma.usecase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_FORMATO_CORREO;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatoCorreoInvalidoException extends RuntimeException {
    public FormatoCorreoInvalidoException() {
        super(ERROR_FORMATO_CORREO);
    }
}
