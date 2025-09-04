package co.com.pragma.model.usuario;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Usuario {

    private String id;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private String contrasena;
    private BigDecimal salarioBase;
    private String idRol;

}
