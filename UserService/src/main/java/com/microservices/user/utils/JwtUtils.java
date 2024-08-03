package com.microservices.user.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

  private static final String SECRET_KEY = "2B4B6250655368566D5971337436763979244226452948404D635166546A576E";

  public String generateAccessToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
            .setClaims(claims).setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .setHeaderParam("token_type", "access_token")
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  public String generateRefreshToken(String subject) {
    return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .setHeaderParam("token_type", "refresh_token")
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  private Key getSigningKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
