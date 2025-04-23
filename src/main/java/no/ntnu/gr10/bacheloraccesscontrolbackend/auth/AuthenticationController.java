package no.ntnu.gr10.bacheloraccesscontrolbackend.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto.LoginRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto.RegisterRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto.AuthenticationResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordPolicyService passwordPolicyService;
  private final PasswordEncoder passwordEncoder;

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
                                  AdministratorService administratorService, PasswordPolicyService passwordPolicyService, PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.administratorService = administratorService;
    this.passwordPolicyService = passwordPolicyService;
    this.passwordEncoder = passwordEncoder;
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
      AuthenticationResponse response = authenticateAndGenerateResponse(authentication);

      return ResponseEntity.ok(response);
    } catch (BadCredentialsException | UsernameNotFoundException e) {
      logger.severe("Invalid credentials: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password"));
    } catch (Exception e) {
      logger.severe("Error during authentication: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during authentication"));
    }
  }

  /**
   * Temporary method to register a new administrator.
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    try {
      if (administratorService.existsByUsername(registerRequest.getUsername())) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Username already exists"));
      }

      passwordPolicyService.validatePassword(registerRequest.getPassword());
      passwordEncoder.encode(registerRequest.getPassword());

      Administrator admin = new Administrator(
              registerRequest.getUsername(),
              registerRequest.getPassword(),
              registerRequest.getFirstName(),
              registerRequest.getLastName()
      );

      admin.setRegistered(true);

      administratorService.createAdministrator(admin);

      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      registerRequest.getUsername(),
                      registerRequest.getPassword()
              )
      );
      AuthenticationResponse response = authenticateAndGenerateResponse(authentication);

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid registration data"));
    } catch (PasswordPolicyService.WeakPasswordException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid password"));
    } catch (Exception e) {
      logger.severe("Error during registration: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred during registration"));
    }
  }

  private AuthenticationResponse authenticateAndGenerateResponse(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateAuthToken(authentication);
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    return new AuthenticationResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getAuthorities(),
            userDetails.getName()
    );
  }
}
