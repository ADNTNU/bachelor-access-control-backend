package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * Response object for authentication requests.
 */
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

  public AuthenticationResponse() {
    // Default constructor for deserialization
  }

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

  public AuthenticationResponse(String token, String refreshToken, long id, String username, String email, Collection<GrantedAuthority> roles, String name) {
    this.token = token;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Collection<GrantedAuthority> getRoles() {
    return roles;
  }

  public void setRoles(Collection<GrantedAuthority> roles) {
    this.roles = roles;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Date emailVerified) {
    this.emailVerified = emailVerified != null ? emailVerified.getTime() : null;
  }

  public String getTokenType() {
    return tokenType;
  }
}
