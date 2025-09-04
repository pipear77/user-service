package co.com.pragma.api.exceptions;

import co.com.pragma.usecase.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampoObligatorioException.class)
    public ProblemDetail handleCampoObligatorio(CampoObligatorioException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Campo obligatorio faltante");
        problem.setDetail(ex.getMessage());
        problem.setProperty("campo", ex.getCampo());
        return problem;
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ProblemDetail handleCorreoDuplicado(CorreoYaRegistradoException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Conflicto de correo");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(FormatoCorreoInvalidoException.class)
    public ProblemDetail handleFormatoCorreo(FormatoCorreoInvalidoException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Formato de correo inválido");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleValidacionesJakarta(ConstraintViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validación fallida");
        problem.setDetail("Datos de entrada inválidos");
        problem.setProperty("violaciones",
                ex.getConstraintViolations().stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .toList());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Error inesperado");
        problem.setDetail(ex.getMessage());
        return problem;
    }
}
