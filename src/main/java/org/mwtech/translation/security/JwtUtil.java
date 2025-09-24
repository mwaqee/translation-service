package org.mwtech.translation.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
  private final String issuer;
  private final int expiryMinutes;
  private final Key key;

  public JwtUtil(@Value("${app.jwt.secret}") String secret,
                 @Value("${app.jwt.issuer}") String issuer,
                 @Value("${app.jwt.expiryMinutes}") int expiryMinutes) {
    this.issuer = issuer; this.expiryMinutes = expiryMinutes;
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String subject){
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expiryMinutes * 60L);
    return Jwts.builder()
      .setIssuer(issuer)
      .setSubject(subject)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public Jws<Claims> parse(String token){
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}
