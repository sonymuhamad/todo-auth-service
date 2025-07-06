package com.sony.authservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;                       // same secret as your gateway!

    @Value("${jwt.ttl-ms:1800000}")               // default 15 min
    private long ttlMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, List<String> roles, Map<String, Object> extraClaims) {
        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .subject(userId)
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlMs))
                .signWith(key());

        if (roles != null) builder.claim("roles", roles);
        if (extraClaims != null) builder.claims(extraClaims);

        return builder.compact();
    }

    public Claims verifyAndGetClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token.startsWith("Bearer ")? token.substring(7): token)
                .getPayload();
    }
}
