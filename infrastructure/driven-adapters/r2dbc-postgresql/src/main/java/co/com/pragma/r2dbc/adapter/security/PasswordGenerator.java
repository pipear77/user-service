package co.com.pragma.r2dbc.adapter.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        String rawPassword = "1234567890";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Hash generado:");
        System.out.println(encoded);
        System.out.println("Longitud: " + encoded.length());
    }
}