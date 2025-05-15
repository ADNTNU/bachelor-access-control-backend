package no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request for a paginated CRUD list operation.
 * This class is used to encapsulate the parameters required for such an operation.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedCrudListRequest {
  @NotNull
  @NotBlank
  private Integer page;

  @NotNull
  @NotBlank
  private Integer size;

  @NotNull
  @NotBlank
  private Long companyId;

}
