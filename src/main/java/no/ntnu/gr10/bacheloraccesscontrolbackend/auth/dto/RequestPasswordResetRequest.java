package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestPasswordResetRequest {

  @NotNull
  @NotBlank
  private String email;

  public RequestPasswordResetRequest() {
    // Default constructor for deserialization
  }

  public RequestPasswordResetRequest(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
