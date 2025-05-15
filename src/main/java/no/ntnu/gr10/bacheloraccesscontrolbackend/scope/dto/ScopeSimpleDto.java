package no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for representing a simple scope.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScopeSimpleDto {
  @NotNull
  @NotBlank

  private String key;
  @NotNull
  @NotBlank

  private String name;
  @NotNull
  @NotBlank
  private String description;
}