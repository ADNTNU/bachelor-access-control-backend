package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateApiKeyResponse {

  @NotBlank
  @NotNull
  private Long id;

  @NotBlank
  @NotNull
  private String clientId;

  @NotBlank
  @NotNull
  private String clientSecret;

  public CreateApiKeyResponse() {
    // Default constructor for serialization
  }

  public CreateApiKeyResponse(Long id, String clientId, String clientSecret) {
    this.id = id;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
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

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
