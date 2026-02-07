package com.cesi.projetindiv.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  private final Key key;
  private final long expirationSeconds;

  public JwtService(
    @Value("${app.jwt.secret-base64}") String secretBase64,
    @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(String subject) {
    Instant now = Instant.now();
    return Jwts.builder()
      .setSubject(subject)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String validateAndExtractSubject(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }
}
