package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DeleteAdministratorCompanyRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @NotEmpty
  private List<Long> administratorIds;


  public DeleteAdministratorCompanyRequest() {
    // Default constructor for deserialization
  }

  public DeleteAdministratorCompanyRequest(Long companyId, List<Long> administratorIds) {
    this.companyId = companyId;
    this.administratorIds = administratorIds;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public List<Long> getAdministratorIds() {
    return administratorIds;
  }

  public void setAdministratorIds(List<Long> administratorIds) {
    this.administratorIds = administratorIds;
  }
}
