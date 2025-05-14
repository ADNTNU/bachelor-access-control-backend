package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for resetting a user's password.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResetPasswordRequest {

  @NotNull
  @NotBlank
  private String token;

  @NotNull
  @NotBlank
  private String newPassword;

}
