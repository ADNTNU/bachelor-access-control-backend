package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResetPasswordRequest {

  @NotNull
  @NotBlank
  private String token;

  @NotNull
  @NotBlank
  private String newPassword;

  public ResetPasswordRequest() {
    // Default constructor for deserialization
  }

  public ResetPasswordRequest(String token, String newPassword) {
    this.token = token;
    this.newPassword = newPassword;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
