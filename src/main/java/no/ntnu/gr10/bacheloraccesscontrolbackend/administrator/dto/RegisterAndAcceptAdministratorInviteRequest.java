package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterAndAcceptAdministratorInviteRequest {
  @NotNull
  @NotBlank
  private String inviteToken;

  @NotNull
  @NotBlank
  private String password;

  @NotNull
  @NotBlank
  private String firstName;

  @NotNull
  @NotBlank
  private String lastName;

  public RegisterAndAcceptAdministratorInviteRequest() {
    // Default constructor for deserialization
  }

  public RegisterAndAcceptAdministratorInviteRequest(String inviteToken, String password, String firstName, String lastName) {
    this.inviteToken = inviteToken;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getInviteToken() {
    return inviteToken;
  }

  public void setInviteToken(String token) {
    this.inviteToken = token;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
