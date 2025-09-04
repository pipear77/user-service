package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
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
@Builder(toBuilder = true)
public class UsuarioEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    private String nombres;
    private String apellidos;

    @Column("numero_documento")
    private String numeroDocumento;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;
    private String telefono;

    @Column("correo_electronico")
    private String correoElectronico;

    private String contrasena;

    @Column("salario_base")
    private BigDecimal salarioBase;

    @Column("id_rol")
    private UUID idRol;

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
