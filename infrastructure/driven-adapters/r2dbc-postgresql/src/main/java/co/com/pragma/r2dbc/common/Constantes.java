package co.com.pragma.r2dbc.common;

public class Constantes {
    private Constantes() {}

    // Usuario
    public static final String USUARIO_NO_GUARDADO = "No se pudo guardar el usuario";
    public static final String USUARIO_NO_ENCONTRADO = "No se pudo encontrar el usuario";
    public static final String USUARIO_NO_ENCONTRADO_POR_DOCUMENTO = "Usuario no encontrado con documento: ";
    public static final String ERROR_CONSULTA_USUARIOS = "Error interno al consultar usuarios";
    public static final String ERROR_CONSULTA_USUARIO_POR_DOCUMENTO = "Error interno al buscar usuario";

    // Rol
    public static final String ROL_NO_ENCONTRADO = "Rol no encontrado";
    public static final String ROL_NO_AUTORIZADO = "No tienes permisos para registrar usuarios";

    // Seguridad
    public static final String TOKEN_REQUERIDO = "Token de autorización requerido";
    public static final String TOKEN_MALFORMADO = "Token no presente o mal formado";

    // Validaciones
    public static final String CAMPO_OBLIGATORIO_FALTANTE = "Campo obligatorio faltante";
    public static final String CORREO_DUPLICADO = "Correo duplicado";
    public static final String FORMATO_CORREO_INVALIDO = "Formato de correo inválido";

    // Errores técnicos
    public static final String ERROR_TECNICO_CONSULTA_USUARIO = "Error técnico al consultar usuario";
    public static final String ERROR_INESPERADO = "Error inesperado";
    public static final String DESCRIPCION_ERROR_INESPERADO = "Ocurrió un error interno. Intenta más tarde.";

    // Constantes para paths
    public static final String BASE = "/api/v1";
    public static final String LOGIN = BASE + "/login";
    public static final String VALIDATE = BASE + "/validate";
    public static final String USUARIOS = BASE + "/usuarios";

}
