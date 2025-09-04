package co.com.pragma.usecase.common;

public class ConstantesUsuario {

    private ConstantesUsuario() {}

    // Mensajes de error
    public static final String ERROR_CORREO_DUPLICADO = "El correo electrónico ya está registrado: ";
    public static final String ERROR_NOMBRE_REQUERIDO = "El nombre es obligatorio";
    public static final String ERROR_APELLIDO_REQUERIDO = "El apellido es obligatorio";
    public static final String ERROR_CONTRASENA_REQUERIDA = "La contraseña es obligatoria";
    public static final String ERROR_DOCUMENTO_REQUERIDO = "El número de documento es obligatorio";
    public static final String ERROR_SALARIO_REQUERIDO = "El salario base es obligatorio";
    public static final String ERROR_CORREO_REQUERIDO = "El correo electrónico es obligatorio";
    public static final String ERROR_FORMATO_CORREO = "El correo electrónico debe tener un formato válido";
    public static final String ERROR_SALARIO_NUMERICO = "El salario base debe ser un valor numérico entre 0 y 15.000.000";

    // Expresiones regulares
    public static final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}