package co.com.pragma.usecase.exceptions;

public class CampoObligatorioException extends RuntimeException {
    private final String campo;

    public CampoObligatorioException(String campo, int codigo) {
        super("Campo obligatorio: " + campo);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
