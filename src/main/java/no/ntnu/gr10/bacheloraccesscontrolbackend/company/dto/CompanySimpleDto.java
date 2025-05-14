package no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple DTO class representing a company with an ID and name.
 * This class is used for transferring company data between different layers of the application.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySimpleDto {

  @NotNull
  @NotBlank
  private Long id;

  @NotNull
  @NotBlank
  private String name;
}
