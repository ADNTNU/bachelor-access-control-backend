package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * Response object for authentication requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

  @NotNull
  @NotBlank
  String token;

  @NotNull
  @NotBlank
  String refreshToken;

  @NotNull
  @NotBlank
  long id;

  @NotNull
  @NotBlank
  String username;

  @NotNull
  @NotBlank
  String email;

  @NotNull
  @NotBlank
  Collection<GrantedAuthority> roles;

  @NotNull
  @NotBlank
  String name;

  Long emailVerified;

  @SuppressWarnings("squid:S1170")
  private final String tokenType = "Bearer";

  /**
   * Constructor for creating an authentication response.
   *
   * @param token        the authentication token
   * @param refreshToken the refresh token
   * @param userDetails  the user details
   */
  public AuthenticationResponse(String token, String refreshToken, CustomUserDetails userDetails) {
    this.token = token;
    this.refreshToken = refreshToken;
    this.id = userDetails.getId();
    this.username = userDetails.getUsername();
    this.email = userDetails.getEmail();
    this.roles = userDetails.getAuthorities();
    this.name = userDetails.getName();
    setEmailVerified(userDetails.getRegistered());
  }

  public void setEmailVerified(Date emailVerified) {
    this.emailVerified = emailVerified != null ? emailVerified.getTime() : null;
  }

}
