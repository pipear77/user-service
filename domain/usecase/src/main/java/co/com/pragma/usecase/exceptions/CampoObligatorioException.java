package co.com.pragma.usecase.exceptions;

public class CampoObligatorioException extends RuntimeException {
    private final String campo;
    private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";

    public String getCampo() {
        return campo;
    }

    public CampoObligatorioException(String campo, int codigo) {
        super(CAMPO_OBLIGATORIO + ": " + campo);
        this.campo = campo;
    }

}
