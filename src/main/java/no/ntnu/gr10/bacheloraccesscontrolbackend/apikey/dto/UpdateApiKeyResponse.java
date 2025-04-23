package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateApiKeyResponse {

  @NotBlank
  @NotNull
  private Long id;

  @NotBlank
  @NotNull
  private String clientId;

  public UpdateApiKeyResponse() {
    // Default constructor for serialization
  }

  public UpdateApiKeyResponse(Long id, String clientId) {
    this.id = id;
    this.clientId = clientId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
}
