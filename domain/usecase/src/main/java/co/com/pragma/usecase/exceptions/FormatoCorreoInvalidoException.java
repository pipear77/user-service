package co.com.pragma.usecase.exceptions;

public class FormatoCorreoInvalidoException extends RuntimeException {
    public FormatoCorreoInvalidoException() {
        super("Formato de correo inválido");
    }
}
