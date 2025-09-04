package co.com.pragma.model.rol;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rol {
    private Long id;
    private String name;
    private String description;
}
