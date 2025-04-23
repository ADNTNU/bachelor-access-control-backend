package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InviteAdministratorRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @Email
  private String username;

  @NotBlank
  @NotNull
  private Boolean enabled;

  @NotBlank
  @NotNull
  private String role;

  public InviteAdministratorRequest() {
    // Default constructor for deserialization
  }

  public InviteAdministratorRequest(Long companyId, String username, Boolean enabled, String role) {
    this.companyId = companyId;
    this.username = username;
    this.enabled = enabled;
    this.role = role;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
