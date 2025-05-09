package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;

public class AdministratorListDto {
  private final long id;
  private final boolean enabled;
  private final boolean accepted;
  private final boolean registered;
  private final String username;
  private final String email;
  private final String name;

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

  public long getId() {
    return id;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public boolean isRegistered() {
    return registered;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }
}
