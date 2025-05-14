package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.ApiKey;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;

import java.util.List;

/**
 * Data Transfer Object (DTO) for each element when listing API keys.
 * This class is used to transfer data between the server and client.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ApiKeyListDto {
  @NotNull
  private final long id;

  @NotNull
  private final boolean enabled;

  @NotNull
  @NotBlank
  private final String clientId;

  @NotNull
  @NotBlank
  private final String name;
  @NotNull
  @NotBlank
  private final String description;
  @NotNull
  @NotBlank
  private final List<String> scopes;

  /**
   * Constructor to create an instance of ApiKeyListDto from an ApiKey object.
   *
   * @param apiKey the ApiKey object to convert
   */
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
}
