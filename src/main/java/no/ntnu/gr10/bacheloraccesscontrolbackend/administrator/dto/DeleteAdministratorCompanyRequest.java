package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for deleting administrator company requests.
 * Contains the company ID and a list of administrator IDs to be deleted.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAdministratorCompanyRequest {

  @NotBlank
  @NotNull
  private Long companyId;

  @NotBlank
  @NotNull
  @NotEmpty
  private List<Long> administratorIds;
  
}
