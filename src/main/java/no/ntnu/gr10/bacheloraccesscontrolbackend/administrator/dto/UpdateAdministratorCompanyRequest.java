package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateAdministratorCompanyRequest {
  @NotNull
  @NotBlank
  private Long companyId;

  @NotNull
  @NotBlank
  private Boolean enabled;

  @NotNull
  @NotBlank
  private String role;

  public UpdateAdministratorCompanyRequest() {
    // Default constructor for deserialization
  }

  public UpdateAdministratorCompanyRequest(Long companyId, Boolean enabled, String role) {
    this.companyId = companyId;
    this.enabled = enabled;
    this.role = role;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
