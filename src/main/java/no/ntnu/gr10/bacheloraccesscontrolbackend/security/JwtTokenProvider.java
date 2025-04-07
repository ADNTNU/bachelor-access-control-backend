package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Administrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for generating and verifying JWT tokens.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Component
public class JwtTokenProvider {

  @Value("${jwt_secret}")
  private String secretKey;


  /**
   * Generates a JWT token for the given authentication.
   * <p>
   *   This method creates a JWT token using the provided authentication information.
   *   The token includes the username, issued date, and expiration date.
   * </p>
   *
   * @param authentication the authentication object containing user details
   * @return the generated JWT token
   * @throws InvalidKeyException if the signing key is invalid
   */
  public String generateToken(Authentication authentication) throws InvalidKeyException {
    Administrator admin = (Administrator) authentication.getPrincipal();

    Date now = new Date();
    // 24h
    long expirationMs = 86400000;
    Date expirationDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(admin.getUsername())
        .issuedAt(now)
        .expiration(expirationDate)
//       TODO: Add claims if needed
//        .setClaims()
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Verifies the given JWT token and retrieves the username from it.
   * <p>
   *   This method checks the validity of the provided JWT token and extracts the username from it.
   * </p>
   *
   * @param token the JWT token to verify
   * @return the username extracted from the token
   * @throws JwtException if the token is invalid or expired
   * @throws IllegalArgumentException if the token is null or empty
   */
  public String verifyTokenAndGetUsername(String token) throws JwtException, IllegalArgumentException {
    return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
  }
}
