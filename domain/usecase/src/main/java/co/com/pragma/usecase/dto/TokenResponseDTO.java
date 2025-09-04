package co.com.pragma.usecase.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
    private String token;
    private String tipo;
    private long expiracion;
}
