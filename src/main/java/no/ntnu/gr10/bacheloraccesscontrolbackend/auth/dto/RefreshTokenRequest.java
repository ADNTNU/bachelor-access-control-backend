package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the refresh token request.
 * This class is used to encapsulate the refresh token that is sent in the request body when
 * requesting a new access token using a refresh token.
 *
 * @author Anders Lund
 * @version 02.05.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

  @NotNull
  @NotBlank
  private String refreshToken;
}
