package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for creating an API key.
 * Contains fields for the API key's properties.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiKeyRequest {

  @NotBlank
  @NotNull
  private Boolean enabled;

  @NotBlank
  @NotNull
  private String name;

  @NotBlank
  @NotNull
  private String description;

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  private List<String> scopes;
}
