package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {
    @Id
    private UUID id;
    private String nombres;
    private String apellidos;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    @Column("correo_electronico")
    private String correoElectronico;
    @Column("salario_base")
    private BigDecimal salarioBase;
}
