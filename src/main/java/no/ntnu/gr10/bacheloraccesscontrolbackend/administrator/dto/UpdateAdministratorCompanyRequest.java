package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for updating an administrator-company relationship.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdministratorCompanyRequest {
  @NotNull
  @NotBlank
  private Long companyId;

  @NotNull
  @NotBlank
  private Boolean enabled;

  @NotNull
  @NotBlank
  private String role;

}
