package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;

public class AdministratorListDto {
  private final long id;
  private final boolean enabled;
  private final boolean accepted;
  private final String username;
  private final String name;

  public AdministratorListDto(AdministratorCompany administratorCompany) {
    Administrator administrator = administratorCompany.getAdministrator();
    this.id = administrator.getId();
    this.enabled = administratorCompany.isEnabled();
    this.accepted = administratorCompany.isAccepted();
    this.username = administrator.getUsername();
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

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }
}
