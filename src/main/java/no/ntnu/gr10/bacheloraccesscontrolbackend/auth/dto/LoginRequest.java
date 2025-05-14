package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for login requests.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotNull
  @NotBlank
  private String usernameOrEmail;

  @NotNull
  @NotBlank
  private String password;
}
