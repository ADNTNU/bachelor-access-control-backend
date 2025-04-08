package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Response object for authentication requests.
 */
public record AuthenticationResponse(String token, long id, String username, Collection<GrantedAuthority> roles, String name) {

  private static final String tokenType = "Bearer";

  public String getTokenType() {
    return tokenType;
  }
}
