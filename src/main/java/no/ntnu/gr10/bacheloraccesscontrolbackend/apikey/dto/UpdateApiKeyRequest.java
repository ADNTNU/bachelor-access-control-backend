package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UpdateApiKeyRequest {
  @NotBlank
  @NotNull
  private boolean enabled;
  @NotBlank
  @NotNull
  private String name;
  @NotBlank
  @NotNull
  private String description;
  @NotBlank
  @NotNull
  private Long companyId;
  @NotBlank
  @NotNull
  private List<String> scopes;

  public UpdateApiKeyRequest() {
    // Default constructor for serialization
  }

  public UpdateApiKeyRequest(boolean enabled, String name, String description, Long companyId, List<String> scopes) {
    this.enabled = enabled;
    this.name = name;
    this.description = description;
    this.companyId = companyId;
    this.scopes = scopes;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }
}
