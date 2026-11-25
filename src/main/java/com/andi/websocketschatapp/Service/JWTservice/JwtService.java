package com.andi.websocketschatapp.Service.JWTservice;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.security.config.Elements.JWT;

@Service
public class JwtService
{
    private final SecretKey key;
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public JwtService(@Value("${jwt.secret}") String secret)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken (String username)
    {
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.ofEpochSecond(System.currentTimeMillis() + EXPIRATION_TIME)))
                .signWith(key)
                .compact();
    }

    public String validateAndGetUsername (String token)
    {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getBody()
                .getSubject();
    }

}
