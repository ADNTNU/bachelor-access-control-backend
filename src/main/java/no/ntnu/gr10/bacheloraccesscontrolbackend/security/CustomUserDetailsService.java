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
public class CustomUserDetailsService implements UserDetailsService {
  private final AdministratorRepository administratorRepository;

  /**
   * Constructor for CustomUserDetailsService.
   *
   * @param administratorRepository the repository for accessing administrator data.
   */
  @Autowired
  public CustomUserDetailsService(AdministratorRepository administratorRepository) {
    this.administratorRepository = administratorRepository;
  }

  /**
   * Loads user details by username.
   *
   * <p>This method retrieves user details from the database based on the provided username.
   * If the user is not found, it throws a UsernameNotFoundException.
   * </p>
   *
   * @param usernameOrEmail the username or email of the user to be loaded.
   * @return UserDetails object containing user information.
   * @throws UsernameNotFoundException if the user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) {
    return new CustomUserDetails(
            administratorRepository.findWithAdministratorCompaniesByUsernameOrEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User not found: " + usernameOrEmail)),
            usernameOrEmail
    );
  }
}
