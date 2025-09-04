package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;

public interface JwtProviderRepository {
    String generateToken(Usuario usuario);
    boolean validateToken(String token);
    String getSubject(String token);
    String getClaim(String token, String claimName);
    long getExpirationTimestamp();
}

