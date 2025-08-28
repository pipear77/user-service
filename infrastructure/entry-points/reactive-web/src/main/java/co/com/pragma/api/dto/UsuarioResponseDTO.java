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
public class UsuarioResponseDTO {
    @Schema(example = "abc123", description = "Identificador único del usuario")
    private String id;

    @Schema(example = "Juan Carlos", description = "Nombres del usuario")
    private String nombres;

    @Schema(example = "Vaca Calisto", description = "Apellidos del usuario")
    private String apellidos;

    @Schema(example = "1990-05-15", description = "Fecha de nacimiento en formato YYYY-MM-DD")
    private LocalDate fechaNacimiento;

    @Schema(example = "Calle 123 #45-67", description = "Dirección del usuario")
    private String direccion;

    @Schema(example = "3114567890", description = "Teléfono del usuario")
    private String telefono;

    @Schema(example = "juan.vaca@example.com", description = "Correo electrónico del usuario")
    private String correoElectronico;

    @Schema(example = "3500000.00", description = "Salario base del usuario")
    private BigDecimal salarioBase;
}
