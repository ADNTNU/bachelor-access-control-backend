package no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCompanyRequest {

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotBlank
  private String administratorUsername;

  public CreateCompanyRequest() {
    // Default constructor for deserialization
  }

  public CreateCompanyRequest(String name, String administratorUsername) {
    this.name = name;
    this.administratorUsername = administratorUsername;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAdministratorUsername() {
    return administratorUsername;
  }

  public void setAdministratorUsername(String administratorUsername) {
    this.administratorUsername = administratorUsername;
  }
}
