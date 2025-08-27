package co.com.pragma.api.exceptions;

import co.com.pragma.usecase.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CampoObligatorioException.class)
    public ResponseEntity<String> handleCampoObligatorio(CampoObligatorioException ex) {
        return ResponseEntity.badRequest().body("Campo obligatorio: " + ex.getMessage());
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<String> handleCorreoDuplicado(CorreoYaRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado: " + ex.getMessage());
    }

    @ExceptionHandler(FormatoCorreoInvalidoException.class)
    public ResponseEntity<String> handleFormatoCorreo(FormatoCorreoInvalidoException ex) {
        return ResponseEntity.badRequest().body("Formato de correo inválido");
    }

    @ExceptionHandler(SalarioFueraDeRangoException.class)
    public ResponseEntity<String> handleSalario(SalarioFueraDeRangoException ex) {
        return ResponseEntity.badRequest().body("Salario fuera de rango");
    }

}
