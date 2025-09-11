package co.com.pragma.api.exceptions;

import co.com.pragma.usecase.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static co.com.pragma.r2dbc.common.Constantes.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CampoObligatorioException.class)
    public ProblemDetail handleCampoObligatorio(CampoObligatorioException ex) {
        log.warn(CAMPO_OBLIGATORIO_FALTANTE + " {}", ex.getCampo());
        return buildProblem(HttpStatus.BAD_REQUEST, CAMPO_OBLIGATORIO_FALTANTE, ex.getMessage(), "campo", ex.getCampo());
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ProblemDetail handleCorreoDuplicado(CorreoYaRegistradoException ex) {
        log.warn(CORREO_DUPLICADO + ":  {}", ex.getMessage());
        return buildProblem(HttpStatus.CONFLICT, CORREO_DUPLICADO, ex.getMessage());
    }

    @ExceptionHandler(FormatoCorreoInvalidoException.class)
    public ProblemDetail handleFormatoCorreo(FormatoCorreoInvalidoException ex) {
        log.warn(FORMATO_CORREO_INVALIDO);
        return buildProblem(HttpStatus.BAD_REQUEST, FORMATO_CORREO_INVALIDO, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleValidacionesJakarta(ConstraintViolationException ex) {
        log.warn("Violaciones de validación: {}", ex.getConstraintViolations());
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
        log.error("Error inesperado", ex);
        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_INESPERADO, DESCRIPCION_ERROR_INESPERADO);
    }

    private ProblemDetail buildProblem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return problem;
    }

    private ProblemDetail buildProblem(HttpStatus status, String title, String detail, String propertyKey, Object propertyValue) {
        ProblemDetail problem = buildProblem(status, title, detail);
        problem.setProperty(propertyKey, propertyValue);
        return problem;
    }
}

