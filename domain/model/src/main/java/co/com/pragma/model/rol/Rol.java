package co.com.pragma.model.rol;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rol {
    private UUID id;
    private String name;
    private String description;
}
