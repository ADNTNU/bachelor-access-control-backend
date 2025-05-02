package no.ntnu.gr10.bacheloraccesscontrolbackend.auth;

import io.jsonwebtoken.security.InvalidKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto.*;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.SuccessResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.JwtTokenProvider;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


/**
 * Controller for handling authentication-related requests.
 * Includes methods for login, logout, and token management.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final AdministratorService administratorService;

  /**
   * Constructor for AuthenticationController.
   *
   * @param authenticationManager the authentication manager
   * @param tokenProvider         the JWT token provider
   * @param administratorService  the administrator service
   */
  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager,
                                  JwtTokenProvider tokenProvider,
                                  AdministratorService administratorService) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.administratorService = administratorService;
  }

  /**
   * Authenticates the user and returns a JWT token if successful.
   * <p>
   *   This method handles the login request by validating the provided username and password.
   *   If the credentials are valid, it generates a JWT token and returns it in the response.
   *   If the credentials are invalid, it returns an unauthorized response.
   *   </p>
   *
   *   @param loginRequest the login request containing username and password
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsernameOrEmail(),
              loginRequest.getPassword()
          )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      AuthenticationResponse response = generateAuthenticationResponse(authentication);

      return ResponseEntity.ok(response);
    } catch (BadCredentialsException | UsernameNotFoundException e) {
      logger.severe("Invalid credentials: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password"));
    } catch (Exception e) {
      logger.severe("Error during authentication: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during authentication"));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    if (authentication != null){
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Requests to refresh the access token using the refresh token.
   * <p>
   *   This method handles the token refresh request by validating the provided refresh token.
   *   If the token is valid, it generates a new access token and returns it in the response.
   *   If the token is invalid, it returns an unauthorized response.
   * </p>
   *
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    try {
      String adminIdString = tokenProvider.verifyAuthTokenAndGetAdminId(refreshTokenRequest.getRefreshToken());
      long adminId = Long.parseLong(adminIdString);
      Administrator admin = administratorService.getById(adminId);
      CustomUserDetails userDetails = new CustomUserDetails(admin, admin.getEmail());

      Authentication authentication = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      AuthenticationResponse response = generateAuthenticationResponse(authentication);

      return ResponseEntity.ok(response);
    } catch (InvalidKeyException e) {
      logger.severe("Invalid refresh token: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid refresh token"));
    } catch (Exception e) {
      logger.severe("Error during token refresh: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during token refresh"));
    }
  }

  @PostMapping("/request-password-reset")
  public ResponseEntity<?> requestPasswordReset(@RequestBody RequestPasswordResetRequest request) {
    try {
      administratorService.requestPasswordReset(request.getEmail());
      return ResponseEntity.ok(new SuccessResponse("Password reset request sent successfully"));
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
    } catch (Exception e) {
      logger.severe("Error during password reset request: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during password reset request"));
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    try {
      administratorService.resetPassword(request.getToken(), request.getNewPassword());
      return ResponseEntity.ok(new SuccessResponse("Password reset successfully"));
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
    } catch (InvalidKeyException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid token"));
    } catch (Exception e) {
      logger.severe("Error during password reset: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during password reset"));
    }
  }

  private AuthenticationResponse generateAuthenticationResponse(Authentication authentication) {
    String accessToken = tokenProvider.generateAuthToken(authentication);
    String refreshToken = tokenProvider.generateRefreshToken(authentication);
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    return new AuthenticationResponse(
            accessToken,
            refreshToken,
            userDetails
    );
  }
}
