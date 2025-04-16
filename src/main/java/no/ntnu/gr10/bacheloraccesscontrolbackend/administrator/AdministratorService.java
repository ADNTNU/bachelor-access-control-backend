package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing administrators.
 * Provides methods for CRUD operations as well as for authentication.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Service
public class AdministratorService {

  private final AdministratorRepository administratorRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AdministratorService(AdministratorRepository administratorRepository,
                              PasswordEncoder passwordEncoder) {
    this.administratorRepository = administratorRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Get a list of Administrators by company ID with pagination.
   * <p>
   * This method retrieves a list of Administrators associated with a specific company ID.
   * It supports pagination by allowing the caller to specify the page number and size.
   * The method returns a paginated list of Administrators.
   * </p>
   *
   * @param companyId the ID of the company whose Administrators are to be retrieved
   * @param page the page number to retrieve
   * @param size the number of API keys per page
   * @return a paginated {@link List} of {@link ApiKey}s associated with the specified company ID
   */
  public List<Administrator> getAdministratorsByCompanyId(long companyId, int page, int size) {
    return administratorRepository.findAdministratorsByCompanyId(companyId, PageRequest.of(page, size));
  }

  /**
   * Get an Administrator by username.
   * <p>
   * This method retrieves an Administrator entity from the database based on the provided username.
   * If the Administrator is not found, it throws a UsernameNotFoundException.
   * </p>
   *
   * @param username the username of the Administrator to be retrieved
   * @return the found {@link Administrator} entity
   * @throws UsernameNotFoundException if the Administrator is not found
   */
  public Administrator getByUsername(String username) {
    return administratorRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**
   * Enable an Administrator.
   * @param administrator the Administrator entity to be enabled
   */
  public void enableAdministrator(Administrator administrator) {
    administrator.setEnabled(true);
//    TODO: Check if we can update the entity without saving it again
//     to avoid overwriting possibly updated fields
    administratorRepository.save(administrator);
  }

  /**
   * Creates a new Administrator.
   * <p>
   *   Creates a new Administrator entity and saves it to the database.
   *   The password is encoded before saving using the PasswordEncoder Bean.
   *   If the username already exists, an IllegalArgumentException is thrown.
   *   </p>
   *
   * @param administrator the Administrator entity to be created
   * @throws IllegalArgumentException if the username already exists
   */
  public Administrator createAdministrator(Administrator administrator) throws IllegalArgumentException {
    // Check if the username already exists
    if (administratorRepository.existsByUsername(administrator.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

//    TODO: Split encoding logic to own method when used in multiple places
    String encodedPassword = passwordEncoder.encode(administrator.getPassword());
    administrator.setPassword(encodedPassword);

    return administratorRepository.save(administrator);
  }

//  TODO: Add methods for creating, updating, and deleting administrators
}
