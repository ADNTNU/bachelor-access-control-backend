package no.ntnu.gr10.bacheloraccesscontrolbackend.services;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.ApiKey;
import no.ntnu.gr10.bacheloraccesscontrolbackend.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing API keys.
 * Has methods for CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Service
public class ApiKeyService {
  final ApiKeyRepository apiKeyRepository;

  @Autowired
  public ApiKeyService(ApiKeyRepository apiKeyRepository) {
    this.apiKeyRepository = apiKeyRepository;
  }

  /**
   * Get a list of API keys by company ID with pagination.
   * <p>
   * This method retrieves a list of API keys associated with a specific company ID.
   * It supports pagination by allowing the caller to specify the page number and size.
   * The method returns a paginated list of API keys.
   * </p>
   *
   * @param companyId the ID of the company whose API keys are to be retrieved
   * @param page the page number to retrieve
   * @param size the number of API keys per page
   * @return a paginated list of API keys associated with the specified company ID
   */
  public List<ApiKey> getApiKeysByCompanyId(long companyId, int page, int size) {
    return apiKeyRepository.findApiKeysByCompanyId(companyId, PageRequest.of(page, size));
  }

//  TODO: Add methods for creating, updating, and deleting API keys
}
