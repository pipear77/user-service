package co.com.pragma.usecase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidacionNulaException extends RuntimeException {
    private static final String VALIDACION_NULA = "La validación retornó null";

    public ValidacionNulaException() {
        super(VALIDACION_NULA);
    }
}
