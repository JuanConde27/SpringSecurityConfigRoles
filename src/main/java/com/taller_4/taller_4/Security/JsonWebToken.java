package com.taller_4.taller_4.Security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Component
public class JsonWebToken {

    private final String jwtSecret = "NX6RScZzYvduC8YktGexQ74FksuPa6h0xVdnvRzOztA=";

    private final Key jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateJwtToken(Long id, String role) {
        Date now = new Date();
        long jwtExpirationMs = 3600000L; // 1 hour
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(id.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    public Long getIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(authToken);
            System.out.println("Token válido");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Token inválido");
        }
        return false;
    }
}
