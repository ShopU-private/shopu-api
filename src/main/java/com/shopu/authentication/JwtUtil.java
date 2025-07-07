package com.shopu.authentication;

import com.shopu.exception.ApplicationException;
import com.shopu.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY_ENCODED;

    private SecretKey SECRET_KEY;

    @PostConstruct
    public void init() {
        if (SECRET_KEY_ENCODED == null) {
            throw new IllegalStateException("JWT secret is not configured properly.");
        }
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY_ENCODED);
        SECRET_KEY = Keys.hmacShaKeyFor(decodedKey);
    }

    // Generate Token
    public String generateAccessToken(String userId, Role role) {

        long ACCESS_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate Access TokenValidateAccessToken
    public boolean validateAccessToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    // Refresh Access Token
    public String refreshAccessToken(String token) {
        if (!validateAccessToken(token)) {
            throw new ApplicationException("Invalid Token");
        }
        String userId = extractUserId(token);
        Role role = extractRole(token);
        return generateAccessToken(userId, role);
    }

    // Extract User ID from Token
    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extract Role from Token
    public Role extractRole(String token) {
        String roleName = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        return Role.valueOf(roleName);
    }
}


