package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;

import java.util.Map;

public interface JwtProviderRepository {
    String generateToken(Usuario usuario);
    boolean validateToken(String token);
    String getSubject(String token);
    String getClaim(String token, String claimName);
    long getExpirationTimestamp();
    String generateToken(String subject, Map<String, Object> claims);
}

