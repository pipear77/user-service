package co.com.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
