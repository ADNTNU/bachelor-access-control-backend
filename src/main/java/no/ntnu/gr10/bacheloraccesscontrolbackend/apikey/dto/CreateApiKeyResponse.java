package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the response of creating an API key.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiKeyResponse {

  @NotBlank
  @NotNull
  private Long id;

  @NotBlank
  @NotNull
  private String clientId;

  @NotBlank
  @NotNull
  private String clientSecret;

}
