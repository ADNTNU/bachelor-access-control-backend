package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
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

  private static final String USERNAME_CLAIM = "username";
  private static final String ROLES_CLAIM = "roles";

  private static final String INVITE_TOKEN_SUBJECT = "invite";
  private static final String COMPANY_ID_CLAIM = "companyId";
  private static final String ADMIN_ID_CLAIM = "adminId";
  private static final String ADMIN_REGISTERED_CLAIM = "adminRegistered";


  /**
   * Generates a JWT token for the given authentication.
   * <p>
   * This method creates a JWT token using the provided authentication information.
   * The token includes the username, issued date, and expiration date.
   * </p>
   *
   * @param authentication the authentication object containing user details
   * @return the generated JWT token
   * @throws InvalidKeyException if the signing key is invalid
   */
  public String generateAuthToken(Authentication authentication) throws InvalidKeyException {
    CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();

    Date now = new Date();
    // 24h
    long expirationMs = 86400000; // 24 hours
    Date expirationDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
            .subject(String.valueOf(admin.getId()))
            .issuedAt(now)
            .expiration(expirationDate)
            .claim(USERNAME_CLAIM, admin.getUsername())
            .claim(ROLES_CLAIM, admin.getAuthorities())
            .signWith(getSigningKey())
            .compact();
  }

  /**
   * Verifies the given JWT token and retrieves the username from it.
   * <p>
   * This method checks the validity of the provided JWT token and extracts the username from it.
   * </p>
   *
   * @param token the JWT token to verify
   * @return the username extracted from the token
   * @throws JwtException             if the token is invalid or expired
   * @throws IllegalArgumentException if the token is null or empty
   */
  public String verifyAuthTokenAndGetUsername(String token) throws JwtException {
    Claims claims = verifyTokenAndGetClaims(token);

    return claims
            .get(USERNAME_CLAIM, String.class);
  }

  public String generateInviteToken(String adminId, String companyId, boolean registered) throws InvalidKeyException {
    Date now = new Date();
    // 24h
    long expirationMs = 1800000; // 30 minutes
    Date expirationDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
            .subject(INVITE_TOKEN_SUBJECT)
            .claim(COMPANY_ID_CLAIM, companyId)
            .claim(ADMIN_ID_CLAIM, adminId)
            .claim(ADMIN_REGISTERED_CLAIM, registered)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSigningKey())
            .compact();
  }

  public Pair<String, String> verifyInviteTokenAndGetCompanyAndAdminId(String token) throws JwtException {
    Claims claims = verifyTokenAndGetClaims(token);

    if (!claims.getSubject().equals(INVITE_TOKEN_SUBJECT)) {
      throw new JwtException("Invalid token subject");
    }

    String companyId = claims
            .get(COMPANY_ID_CLAIM, String.class);
    String adminId = claims
            .get(ADMIN_ID_CLAIM, String.class);

    return Pair.of(companyId, adminId);
  }

  private Claims verifyTokenAndGetClaims(String token) throws JwtException {
    if (token == null || token.isEmpty()) {
      throw new JwtException("Token cannot be null or empty");
    }

    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
  }
}
