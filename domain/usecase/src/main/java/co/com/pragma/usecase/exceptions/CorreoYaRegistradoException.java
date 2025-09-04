package co.com.pragma.usecase.exceptions;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}

