package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;

public class AcceptAdministratorInviteRequest {

  @NotBlank
  private String inviteToken;

  public AcceptAdministratorInviteRequest() {
    // Default constructor for deserialization
  }

  public AcceptAdministratorInviteRequest(String inviteToken) {
    this.inviteToken = inviteToken;
  }

  public String getInviteToken() {
    return inviteToken;
  }

  public void setInviteToken(String token) {
    this.inviteToken = token;
  }
}
