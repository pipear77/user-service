package co.com.pragma.usecase.exceptions;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String correo) {
        super("El correo '" + correo + "' ya está registrado");
    }
}

