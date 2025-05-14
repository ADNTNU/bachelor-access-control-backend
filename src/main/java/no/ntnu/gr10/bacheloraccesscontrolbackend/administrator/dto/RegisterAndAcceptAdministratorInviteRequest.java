package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO for registering a new user and accepting an administrator invite.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAndAcceptAdministratorInviteRequest {
  @NotNull
  @NotBlank
  private String inviteToken;

  @NotNull
  @NotBlank
  private String username;

  @NotNull
  @NotBlank
  private String password;

  @NotNull
  @NotBlank
  private String firstName;

  @NotNull
  @NotBlank
  private String lastName;

}
