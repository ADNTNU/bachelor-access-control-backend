package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for inviting an administrator to a company.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteAdministratorRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @Email
  private String email;

  @NotBlank
  @NotNull
  private Boolean enabled;

  @NotBlank
  @NotNull
  private String role;

}
