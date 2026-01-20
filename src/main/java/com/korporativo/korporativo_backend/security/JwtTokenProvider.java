package com.korporativo.korporativo_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation1234567890}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")  // 24 horas por defecto
    private int jwtExpirationMs;

    /**
     * Genera un JWT token válido por 24 horas
     */
    public String generateToken(String username, String role) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el username (subject) del token
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Extrae el role del token
     */
    public String getRoleFromToken(String token) {
        return (String) Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }

    /**
     * Valida si el token es válido (no expirado, firma correcta, etc)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT expirado: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT no soportado: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.err.println("JWT malformado: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.err.println("Firma JWT inválida: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("JWT vacío: " + e.getMessage());
            return false;
        }
    }
}
