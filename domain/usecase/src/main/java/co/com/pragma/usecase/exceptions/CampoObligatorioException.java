package co.com.pragma.usecase.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CampoObligatorioException extends RuntimeException {
    private final String campo;
    private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";

    public CampoObligatorioException(String campo, int codigo) {
        super(CAMPO_OBLIGATORIO + campo);
        this.campo = campo;
    }

}
