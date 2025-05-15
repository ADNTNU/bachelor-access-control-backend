package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Custom implementation of the UserDetails interface.
 * This class represents the user details used by Spring Security for
 * authentication and authorization.
 * It contains the username, password, enabled status, and authorities (roles) of the user.
 * The constructor takes an Administrator object and initializes the user details.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
@Getter
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
}
