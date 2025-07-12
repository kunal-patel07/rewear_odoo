package com.example.cafe_backedn.services;

import com.example.cafe_backedn.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${jwt.expireIn}")
    private Long expireTime;

    public String generateToken(UserEntity user){
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // sets "typ": "JWT"
                .subject(String.valueOf(user.getId()))
                .claim("email",user.getEmail())
                .claim("role","user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * expireTime))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}

