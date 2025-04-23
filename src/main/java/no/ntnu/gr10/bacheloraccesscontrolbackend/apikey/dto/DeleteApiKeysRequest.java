package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DeleteApiKeysRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @NotEmpty
  private List<Long> apiKeyIds;


  public DeleteApiKeysRequest() {
    // Default constructor for deserialization
  }

  public DeleteApiKeysRequest(Long companyId, List<Long> apiKeyIds) {
    this.companyId = companyId;
    this.apiKeyIds = apiKeyIds;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public List<Long> getApiKeyIds() {
    return apiKeyIds;
  }

  public void setApiKeyIds(List<Long> apiKeyIds) {
    this.apiKeyIds = apiKeyIds;
  }
}
