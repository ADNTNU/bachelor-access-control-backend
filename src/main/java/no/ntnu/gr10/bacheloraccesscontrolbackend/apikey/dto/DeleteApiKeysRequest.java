package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for deleting API keys.
 * Contains the company ID and a list of API key IDs to be deleted.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteApiKeysRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @NotEmpty
  private List<Long> apiKeyIds;

}
