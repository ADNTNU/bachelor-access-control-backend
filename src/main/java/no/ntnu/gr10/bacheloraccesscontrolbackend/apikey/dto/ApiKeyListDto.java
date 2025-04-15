package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.ApiKey;


public class ApiKeyListDto {
  private final long id;
  private final String clientId;
  private final String name;
  private final String description;
  private final long companyId;

  public ApiKeyListDto(ApiKey apiKey) {
    id = apiKey.getId();
    clientId = apiKey.getClientId();
    name = apiKey.getName();
    description = apiKey.getDescription();
    companyId = apiKey.getCompany().getId();
  }

  public long getId() {
    return id;
  }

  public String getClientId() {
    return clientId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public long getCompanyId() {
    return companyId;
  }
}
