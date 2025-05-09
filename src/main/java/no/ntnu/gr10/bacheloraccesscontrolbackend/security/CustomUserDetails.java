package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom implementation of the UserDetails interface.
 * This class represents the user details used by Spring Security for authentication and authorization.
 * It contains the username, password, enabled status, and authorities (roles) of the user.
 * The constructor takes an Administrator object and initializes the user details.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
public class CustomUserDetails implements UserDetails {

  private final long id;
  private final String loginIdentifier;
  private final String username;
  private final String email;
  private final String password;
  private final boolean enabled;
  private final List<GrantedAuthority> authorities;
  private final String name;
  private final Set<Long> companyIds;
  private final Date registered;

  /**
   * Constructor that initializes the user details from an Administrator object.
   *
   * @param administrator the Administrator object containing user details
   */
  public CustomUserDetails(Administrator administrator, String loginIdentifier) {
    this.id = administrator.getId();
    this.loginIdentifier = loginIdentifier;
    this.username = administrator.getUsername();
    this.email = administrator.getEmail();
    this.password = administrator.getPassword();
    this.enabled = administrator.isRegistered();
    this.name = String.format("%s %s", administrator.getFirstName(), administrator.getLastName());
    this.companyIds = administrator.getAdministratorCompanies().stream()
            .map(AdministratorCompany::getCompany)
            .map(Company::getId)
            .collect(Collectors.toSet());
    this.authorities = new LinkedList<>();
    this.registered = administrator.getRegistered();
  }

  /**
   * Gets the ID of the user.
   *
   * @return the ID of the user
   */
  public long getId() {
    return id;
  }

  @Override
  @SuppressWarnings("java:S4275")
  public String getUsername() {
    return loginIdentifier;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public String getActualUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public Set<Long> getCompanyIds() {
    return companyIds;
  }

  public @NotNull @NotBlank Date getRegistered() {
    return registered;
  }
}
