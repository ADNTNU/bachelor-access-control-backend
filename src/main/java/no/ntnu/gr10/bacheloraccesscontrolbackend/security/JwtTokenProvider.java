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

  private static final int ACCESS_TOKEN_EXPIRATION_MS = 900000; // 15 minutes
  private static final int REFRESH_TOKEN_EXPIRATION_MS = 604800000; // 7 days

  private static final String USERNAME_CLAIM = "username";
  private static final String EMAIL_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";

  private static final String TOKEN_TYPE_CLAIM = "tokenType";

  private static final String INVITE_TOKEN_TYPE = "invite";
  private static final String INVITE_COMPANY_ID_CLAIM = "companyId";
  private static final String INVITE_ADMIN_REGISTERED_CLAIM = "adminRegistered";

  private static final String PASSWORD_RESET_TOKEN_TYPE = "passwordReset";
  public static final int PASSWORD_RESET_TOKEN_EXPIRATION_MS = 1800000; // 30 minutes

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
    Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);

    return Jwts.builder()
            .subject(String.valueOf(admin.getId()))
            .issuedAt(now)
            .expiration(expirationDate)
            .claim(USERNAME_CLAIM, admin.getUsername())
            .claim(EMAIL_CLAIM, admin.getEmail())
            .claim(ROLES_CLAIM, admin.getAuthorities())
            .signWith(getSigningKey())
            .compact();
  }

  /**
   * Generates a refresh token for the given authentication.
   * <p>
   *   This method creates a JWT token using the provided authentication information.
   *   The token includes the adminId, issued date, and expiration date.
   * </p>
   *
   * @param authentication the authentication object containing user details
   * @return the generated JWT token
   * @throws InvalidKeyException if the signing key is invalid
   */
  public String generateRefreshToken(Authentication authentication) throws InvalidKeyException {
    CustomUserDetails admin = (CustomUserDetails) authentication.getPrincipal();

    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);

    return Jwts.builder()
            .subject(String.valueOf(admin.getId()))
            .issuedAt(now)
            .expiration(expirationDate)
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
  public String verifyAccessTokenAndGetUsername(String token) throws JwtException {
    Claims claims = verifyTokenAndGetClaims(token);

    return claims
            .get(USERNAME_CLAIM, String.class);
  }

  /**
   * Verifies the given JWT token and retrieves the admin ID from it.
   * <p>
   * This method checks the validity of the provided JWT token and extracts the admin ID from it.
   * </p>
   *
   * @param token the JWT token to verify
   * @return the admin ID extracted from the token
   * @throws JwtException             if the token is invalid or expired
   */
  public String verifyAuthTokenAndGetAdminId(String token) throws JwtException {
    Claims claims = verifyTokenAndGetClaims(token);

    return claims
            .getSubject();
  }

  /**
   * Generates an invitation token for the given admin and company.
   * <p>
   * This method creates a JWT token for inviting an admin to a company.
   * The token includes the company ID, admin ID and registration status.
   * </p>
   *
   * @param adminId    the ID of the admin
   * @param companyId  the ID of the company
   * @param registered whether the admin is registered or not
   * @return the generated invite token
   * @throws InvalidKeyException if the signing key is invalid
   */
  public String generateInviteToken(String adminId, String companyId, boolean registered) throws InvalidKeyException {
    Date now = new Date();
    // 24h
    long expirationMs = 1800000; // 30 minutes
    Date expirationDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
            .subject(adminId)
            .claim(INVITE_COMPANY_ID_CLAIM, companyId)
            .claim(INVITE_ADMIN_REGISTERED_CLAIM, registered)
            .claim(TOKEN_TYPE_CLAIM, INVITE_TOKEN_TYPE)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSigningKey())
            .compact();
  }

  /**
   * Verifies the given invite token and retrieves the company ID and admin ID from it.
   * <p>
   * This method checks the validity of the provided invite token and extracts the company ID and admin ID from it.
   * </p>
   *
   * @param token the invite token to verify
   * @return a pair containing the company ID and admin ID
   * @throws JwtException             if the token is invalid or expired
   * @throws IllegalArgumentException if the token is null or empty
   */
  public Pair<Long, Long> verifyInviteTokenAndGetCompanyAndAdminId(String token) throws JwtException {

    try {
      Claims claims = verifyTokenAndGetClaims(token);

      if (!claims.get(TOKEN_TYPE_CLAIM, String.class).equals(INVITE_TOKEN_TYPE)) {
        throw new JwtException("Invalid token subject");
      }

      String companyIdString = claims
              .get(INVITE_COMPANY_ID_CLAIM, String.class);
      String adminIdString = claims
              .getSubject();

      long companyId = Long.parseLong(companyIdString);
      long adminId = Long.parseLong(adminIdString);

      return Pair.of(companyId, adminId);

    } catch (NumberFormatException e) {
      throw new JwtException("Invalid token format", e);
    }
  }

  /**
   * Generates a password reset token for the given admin.
   * <p>
   * This method creates a JWT token for resetting the password of an admin.
   * The token includes the admin ID and expiration date.
   * </p>
   *
   * @param adminId the ID of the admin
   * @return the generated password reset token
   * @throws InvalidKeyException if the signing key is invalid
   */
  public String generatePasswordResetToken(String adminId) throws InvalidKeyException {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + PASSWORD_RESET_TOKEN_EXPIRATION_MS);

    return Jwts.builder()
            .subject(adminId)
            .claim(TOKEN_TYPE_CLAIM, PASSWORD_RESET_TOKEN_TYPE)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSigningKey())
            .compact();
  }

  /**
   * Verifies the given password reset token and retrieves the admin ID from it.
   * <p>
   * This method checks the validity of the provided password reset token and extracts the admin ID from it.
   * </p>
   *
   * @param token the password reset token to verify
   * @return the admin ID extracted from the token
   * @throws JwtException             if the token is invalid or expired
   * @throws IllegalArgumentException if the token is null or empty
   */
  public String verifyPasswordResetTokenAndGetAdminId(String token) throws JwtException {
    Claims claims = verifyTokenAndGetClaims(token);

    if (!claims.get(TOKEN_TYPE_CLAIM, String.class).equals(PASSWORD_RESET_TOKEN_TYPE)) {
      throw new JwtException("Invalid token type");
    }

    return claims
            .getSubject();
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
