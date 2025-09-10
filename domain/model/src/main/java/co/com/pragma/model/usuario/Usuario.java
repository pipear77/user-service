package co.com.pragma.model.usuario;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Usuario {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correo;
    private String contrasena;
    private BigDecimal salarioBase;
    private UUID idRol;

}
