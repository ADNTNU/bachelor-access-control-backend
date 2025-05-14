package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for requesting a password reset.
 *
 * @author Anders Lund
 * @version 02.05.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPasswordResetRequest {

  @NotNull
  @NotBlank
  private String email;
}
