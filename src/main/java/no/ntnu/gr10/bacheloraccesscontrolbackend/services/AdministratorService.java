package no.ntnu.gr10.bacheloraccesscontrolbackend.services;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.repositories.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @Autowired
  public AdministratorService(AdministratorRepository administratorRepository) {
    this.administratorRepository = administratorRepository;
  }

  /**
   * Find an Administrator by username.
   * <p>
   *   This method retrieves an Administrator entity from the database based on the provided username.
   *   If the Administrator is not found, it throws a UsernameNotFoundException.
   * </p>
   * @param username the username of the Administrator to be found
   * @return the found {@link Administrator} entity
   */
  public Administrator findByUsername(String username) {
    return administratorRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
   * @return a paginated {@link List} of API keys associated with the specified company ID
   */
  public List<Administrator> getAdministratorsByCompanyId(long companyId, int page, int size) {
    return administratorRepository.findAdministratorsByCompanyId(companyId, PageRequest.of(page, size));
  }

//  TODO: Add methods for creating, updating, and deleting administrators
}
