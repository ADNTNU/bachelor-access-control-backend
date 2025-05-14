package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for accepting an administrator invite.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptAdministratorInviteRequest {

  @NotBlank
  private String inviteToken;

}
