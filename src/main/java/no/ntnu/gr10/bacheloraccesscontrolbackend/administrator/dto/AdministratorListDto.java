package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;


/**
 * Data Transfer Object (DTO) for used when sending a list of administrators.
 * This class is used in a {@link java.util.List} when sending a list of administrators.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AdministratorListDto {

  @NotNull
  private final long id;

  @NotNull
  private final Boolean enabled;

  @NotNull
  private final Boolean accepted;

  @NotNull
  private final Boolean registered;

  @NotNull
  @NotBlank
  private final String username;

  @NotNull
  @NotBlank
  private final String email;

  @NotNull
  @NotBlank
  private final String name;

  /**
   * Constructor for creating a new AdministratorListDto.
   *
   * @param administratorCompany The administrator company entity to create the DTO from.
   */
  public AdministratorListDto(AdministratorCompany administratorCompany) {
    Administrator administrator = administratorCompany.getAdministrator();
    this.id = administrator.getId();
    this.enabled = administratorCompany.isEnabled();
    this.accepted = administratorCompany.isAccepted();
    this.registered = administrator.isRegistered();
    this.username = administrator.getUsername();
    this.email = administrator.getEmail();
    this.name = administrator.getFirstName() + " " + administrator.getLastName();
  }
}
