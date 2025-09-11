package co.com.pragma.usecase.exceptions;

import co.com.pragma.usecase.common.ConstantesUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static co.com.pragma.usecase.common.ConstantesUsuario.CREDENCIALES_INVALIDAS;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super(ConstantesUsuario.CREDENCIALES_INVALIDAS);
    }

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
