package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsuarioResponseDTO", description = "Datos del usuario registrado")
public class UsuarioResponseDTO {

    @Schema(example = "a1b2c3d4-e5f6-7890-abcd-1234567890ef", description = "Identificador único del usuario")
    private String id;

    @Schema(example = "Juan", description = "Nombres del usuario")
    private String nombres;

    @Schema(example = "Vaca Calisto", description = "Apellidos del usuario")
    private String apellidos;

    @Schema(example = "123456789", description = "Número de documento")
    private String numeroDocumento;

    @Schema(example = "1990-05-15", description = "Fecha de nacimiento")
    private LocalDate fechaNacimiento;

    @Schema(example = "Calle 123 #45-67", description = "Dirección del usuario")
    private String direccion;

    @Schema(example = "3114567890", description = "Teléfono del usuario")
    private String telefono;

    @Schema(example = "juan.vaca@example.com", description = "Correo electrónico")
    private String correo;

    @Schema(example = "3500000.00", description = "Salario base")
    private BigDecimal salarioBase;

    @Schema(example = "ROL-ADMIN", description = "Identificador del rol")
    private String idRol;
}
