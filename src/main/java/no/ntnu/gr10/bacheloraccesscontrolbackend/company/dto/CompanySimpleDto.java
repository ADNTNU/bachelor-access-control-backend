package no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompanySimpleDto {

  @NotNull
  @NotBlank
  private Long id;

  @NotNull
  @NotBlank
  private String name;

  public CompanySimpleDto() {
    // Default constructor for deserialization
  }

  public CompanySimpleDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
