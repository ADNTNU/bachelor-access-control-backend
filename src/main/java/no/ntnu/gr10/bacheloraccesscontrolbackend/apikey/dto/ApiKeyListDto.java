package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.ApiKey;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;

import java.util.List;


public class ApiKeyListDto {
  private final long id;
  private final boolean enabled;
  private final String clientId;
  private final String name;
  private final String description;
  private final List<String> scopes;

  public ApiKeyListDto(ApiKey apiKey) {
    id = apiKey.getId();
    enabled = apiKey.isEnabled();
    clientId = apiKey.getClientId();
    name = apiKey.getName();
    description = apiKey.getDescription();
    scopes = apiKey.getScopes().stream()
        .map(Scope::getKey)
        .toList();
  }

  public long getId() {
    return id;
  }

  public boolean isEnabled() {
    return enabled;
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

  public List<String> getScopes() {
    return scopes;
  }
}
