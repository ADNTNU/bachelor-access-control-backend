package no.ntnu.gr10.bacheloraccesscontrolbackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for success responses.
 * Contains a message indicating the success of an operation.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuccessResponse {
  @NotNull
  @NotBlank
  private String message;
}
