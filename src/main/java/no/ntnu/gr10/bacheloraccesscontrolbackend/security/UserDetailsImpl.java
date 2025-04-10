package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom implementation of the UserDetails interface.
 * This class represents the user details used by Spring Security for authentication and authorization.
 * It contains the username, password, enabled status, and authorities (roles) of the user.
 * The constructor takes an Administrator object and initializes the user details.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
public class UserDetailsImpl implements UserDetails {

  private final long id;
  private final String username;
  private final String password;
  private final boolean enabled;
  private final List<GrantedAuthority> authorities = new LinkedList<>();
  private final String name;

  /**
   * Constructor that initializes the user details from an Administrator object.
   *
   * @param administrator the Administrator object containing user details
   */
  public UserDetailsImpl(Administrator administrator) {
    this.id = administrator.getId();
    this.username = administrator.getUsername();
    this.password = administrator.getPassword();
    this.enabled = administrator.isEnabled();
    this.name = String.format("%s %s", administrator.getFirstName(), administrator.getLastName());
//    convertRolesToAuthorities(administrator.getRoles());
  }

//  private void convertRolesToAuthorities(Set<Role> roles) {
//    for (Role role : roles) {
//      authorities.add(new SimpleGrantedAuthority(role.getName()));
//    }
//  }

  /**
   * Gets the ID of the user.
   *
   * @return the ID of the user
   */
  public long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
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

  public String getName() {
    return name;
  }
}
