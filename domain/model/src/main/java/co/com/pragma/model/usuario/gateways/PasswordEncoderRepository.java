package co.com.pragma.model.usuario.gateways;

public interface PasswordEncoderRepository {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

