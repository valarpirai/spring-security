package com.example.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final static String SECRET = "91FDFC3EB9EAF3E78C12EE0169A2866A8ED0B595A4F9F1EDF7CF9DEDD37771CBD44886483C5F6C69701D56C0E67390B84D3D763E304538FC4FB3D0662211668B";

    public String generateToken(UserDetails user, String timestampStr) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining()
                )
        );
        claims.put("iss", "Spring security app");
        claims.put("timestamp", timestampStr);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(SessionService.SESSION_TTL)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractTimestamp(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            if (claims.getExpiration().after(Date.from(Instant.now()))) {
                return claims.get("timestamp", String.class);
            }
        } catch (ExpiredJwtException ignored) {}
        return null;
    }

    public String extractUsername(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            if (claims.getExpiration().after(Date.from(Instant.now()))) {
                return claims.getSubject();
            }
        } catch (ExpiredJwtException ignored) {}
        return null;
    }

    public String[] extractSessionData(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            if (claims.getExpiration().after(Date.from(Instant.now()))) {
                return new String[] { claims.getSubject(), claims.get("timestamp", String.class) };
            }
        } catch (ExpiredJwtException ignored) {}
        return null;
    }

    private Claims getClaims(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claims;
    }
}
