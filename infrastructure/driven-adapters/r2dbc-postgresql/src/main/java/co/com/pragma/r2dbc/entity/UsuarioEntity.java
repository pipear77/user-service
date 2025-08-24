package co.com.pragma.r2dbc.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {
    @Id
    private String id;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;

}
