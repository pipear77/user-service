package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del usuario autenticado extraídos del token")
public class UsuarioAutenticadoDTO {

    @Schema(description = "ID del usuario", example = "d6e63d7-...")
    private String id;

    @Schema(description = "Correo electrónico del usuario", example = "leidy@example.com")
    private String correo;

    @Schema(description = "Número de documento de identidad", example = "1234567890")
    private String documentoIdentidad;

    @Schema(description = "Nombres del usuario", example = "Juan David")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez")
    private String apellidos;

    @Schema(description = "Rol del usuario", example = "ROL_CLIENTE")
    private String rol;

    @Schema(description = "Estado del usuario", example = "ACTIVO")
    private String estado;

    @Schema(description = "Indica si la sesión está activa", example = "true")
    private boolean sesionActiva;

    @Schema(description = "Indica el salario base", example = "350000.00")
    private BigDecimal salarioBase;
}