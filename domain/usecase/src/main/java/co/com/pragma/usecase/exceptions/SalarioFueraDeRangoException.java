package co.com.pragma.usecase.exceptions;

import java.math.BigDecimal;

public class SalarioFueraDeRangoException extends RuntimeException {
    public SalarioFueraDeRangoException(BigDecimal min, BigDecimal max) {
        super("El salario base debe estar entre " + min + " y " + max);
    }
}
