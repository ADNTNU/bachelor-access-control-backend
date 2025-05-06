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
  private String email;

  @NotBlank
  @NotNull
  private Boolean enabled;

  @NotBlank
  @NotNull
  private String role;

  public InviteAdministratorRequest() {
    // Default constructor for deserialization
  }

  public InviteAdministratorRequest(Long companyId, String email, Boolean enabled, String role) {
    this.companyId = companyId;
    this.email = email;
    this.enabled = enabled;
    this.role = role;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
