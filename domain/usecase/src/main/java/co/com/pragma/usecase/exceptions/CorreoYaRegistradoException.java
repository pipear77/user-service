package co.com.pragma.usecase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static co.com.pragma.usecase.common.ConstantesUsuario.ERROR_CORREO_DUPLICADO;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String mensaje){
        super(mensaje);
    }
}

