package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ListAdministratorsRequest {

  @NotNull
  @NotBlank
  private Integer page;

  @NotNull
  @NotBlank
  private Integer size;

  @NotNull
  @NotBlank
  private Long companyId;

  public ListAdministratorsRequest() {
    // Default constructor for deserialization
  }

  public ListAdministratorsRequest(Integer page, Integer size, Long companyId) {
    this.page = page;
    this.size = size;
    this.companyId = companyId;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }
}
