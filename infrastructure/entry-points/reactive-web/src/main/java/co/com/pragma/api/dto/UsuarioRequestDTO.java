package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsuarioRequestDTO", description = "Datos para registrar un usuario")
public class UsuarioRequestDTO {

    @NotBlank
    @Schema(example = "Juan", description = "Nombres del usuario")
    private String nombres;

    @NotBlank
    @Schema(example = "Vaca Calisto", description = "Apellidos del usuario")
    private String apellidos;

    @NotBlank
    @Schema(example = "123456789", description = "Número de documento")
    private String numeroDocumento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "1990-05-15", description = "Fecha de nacimiento")
    private LocalDate fechaNacimiento;

    @Schema(example = "Calle 123 #45-67", description = "Dirección del usuario")
    private String direccion;

    @Schema(example = "3114567890", description = "Teléfono del usuario")
    private String telefono;

    @NotBlank
    @Email
    @Schema(example = "juan.vaca@example.com", description = "Correo electrónico")
    private String correo;

    @NotBlank
    @Schema(example = "claveSegura123", description = "Contraseña del usuario")
    private String contrasena;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("15000000.00")
    @Schema(example = "3500000.00", description = "Salario base")
    private BigDecimal salarioBase;

    @NotBlank
    @Schema(example = "ROL-ADMIN", description = "Identificador del rol")
    private String idRol;
}
