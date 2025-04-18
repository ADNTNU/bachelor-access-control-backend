package no.ntnu.gr10.bacheloraccesscontrolbackend.security;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading user-specific data.
 * This class implements the UserDetailsService interface and is used by Spring Security
 * to retrieve user details for authentication and authorization.
 *
 * @author Anders Lund
 * @version 06.04.2025
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AdministratorRepository administratorRepository;

  @Autowired
  public UserDetailsServiceImpl(AdministratorRepository administratorRepository) {
    this.administratorRepository = administratorRepository;
  }

  /**
   * Loads user details by username.
   * <p>
   *   This method retrieves user details from the database based on the provided username.
   *   If the user is not found, it throws a UsernameNotFoundException.
   *   </p>
   * @param username the username identifying the user whose data is required.
   * @return UserDetails object containing user information.
   * @throws UsernameNotFoundException if the user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) {
    return new UserDetailsImpl(
            administratorRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
  }
}
