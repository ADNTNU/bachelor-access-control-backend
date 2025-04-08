package no.ntnu.gr10.bacheloraccesscontrolbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.LoginRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.RegisterRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.responses.AuthenticationResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.JwtTokenProvider;
import no.ntnu.gr10.bacheloraccesscontrolbackend.services.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


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
              loginRequest.username(),
              loginRequest.password()
          )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.generateToken(authentication);
      return ResponseEntity.ok(new AuthenticationResponse(jwt));
    } catch (BadCredentialsException badCredentialsException) {
      System.err.println("Invalid credentials: " + badCredentialsException.getMessage());
      return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      System.err.println("Error during authentication: " + e.getMessage());
      return ResponseEntity.status(500).body("An error occurred during authentication");
    }
  }

  /**
   * Temporary method to register a new administrator.
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    Administrator admin;

    try {
      admin = new Administrator(
              registerRequest.username(),
              registerRequest.password(),
              registerRequest.firstName(),
              registerRequest.lastName()
      );
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration data");
    }

    try {
      administratorService.createAdministrator(admin);

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              registerRequest.username(),
              registerRequest.password()
          )
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      String jwt = tokenProvider.generateToken(authentication);
      return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticationResponse(jwt));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
    } catch (Exception e) {
      System.err.println("Error during registration: " + e.getMessage());
      return ResponseEntity.status(500).body("An error occurred during registration");
    }
  }
}
