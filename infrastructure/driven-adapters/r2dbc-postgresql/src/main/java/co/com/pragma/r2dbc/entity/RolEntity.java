package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "roles", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolEntity {
    @Id
    private String id;
    private String name;
    private String description;
}

