package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.ApiKeyListDto;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.CreateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.CreateApiKeyResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.DeleteApiKeysRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.UpdateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.UpdateApiKeyResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ListResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.PaginatedCrudListRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ApiKeyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
  private final CompanyService companyService;
  private final ScopeService scopeService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructor for ApiKeyService.
   *
   * @param apiKeyRepository the repository for API keys
   * @param companyService   the service for managing companies
   * @param scopeService     the service for managing scopes
   * @param passwordEncoder  the password encoder for encoding secrets
   */
  @Autowired
  public ApiKeyService(
          ApiKeyRepository apiKeyRepository,
          CompanyService companyService,
          ScopeService scopeService,
          PasswordEncoder passwordEncoder
  ) {
    this.apiKeyRepository = apiKeyRepository;
    this.companyService = companyService;
    this.scopeService = scopeService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Get a list of API keys by company ID with pagination.
   *
   * <p>This method retrieves a list of API keys associated with a specific company ID.
   * It supports pagination by allowing the caller to specify the page number and size.
   * The method returns a paginated list of API keys.
   * </p>
   *
   * @param request the request object containing the company ID, page number, and size
   * @return a paginated list of API keys associated with the specified company ID
   */
  public ListResponse<ApiKeyListDto> getListOfApiKeysByCompanyId(PaginatedCrudListRequest request) {
    int pageIndex = Math.max(0, request.getPage() - 1);
    Pageable pageable = PageRequest.of(pageIndex, request.getSize());

    Page<ApiKey> page = apiKeyRepository.findPageableByCompanyId(request.getCompanyId(), pageable);

    return new ListResponse<>(
            page.getContent().stream()
                    .map(ApiKeyListDto::new)
                    .toList(),
            page.getTotalPages(),
            page.getTotalElements()
    );
  }

  /**
   * Get a list of API keys by their IDs and company ID.
   *
   * <p>This method retrieves a list of API keys based on the provided list of API key IDs
   * and the company ID. It returns a list of API keys that match the specified criteria.
   * </p>
   *
   * @param apiKeyIds the list of API key IDs to search for
   * @param companyId the ID of the company associated with the API keys
   * @return a list of API keys matching the specified criteria
   */
  public List<ApiKey> getApiKeysByApiKeyIdsAndCompanyId(List<Long> apiKeyIds, long companyId) {
    return apiKeyRepository.findByApiKeyIdsAndCompanyId(apiKeyIds, companyId);
  }

  /**
   * Create a new API key.
   *
   * <p>This method creates a new API key based on the provided request object.
   * It generates a unique API key and saves it to the database.
   * </p>
   *
   * @param createApiKeyRequest the request object containing the details for creating the API key
   * @return the response body containing the created API key
   * @throws CompanyNotFoundException if the company with the specified ID does not exist
   * @throws ScopeNotFoundException   if any of the specified scopes do not exist
   */
  @Transactional
  public CreateApiKeyResponse createApiKey(CreateApiKeyRequest createApiKeyRequest)
          throws CompanyNotFoundException, ScopeNotFoundException {
    Company company = companyService.getCompanyById(createApiKeyRequest.getCompanyId());

    List<Scope> scopes = createApiKeyRequest.getScopes().stream()
            .map(scopeService::getScopeByScopeKey)
            .toList();

    String clientId = generateUuid();
    String clientSecret = generateUuid();
    String encodedClientSecret = passwordEncoder.encode(clientSecret);

    ApiKey apiKey = new ApiKey(
            createApiKeyRequest.getEnabled(),
            company,
            createApiKeyRequest.getName(),
            createApiKeyRequest.getDescription(),
            clientId,
            encodedClientSecret
    );

    apiKey.setScopes(scopes);

    apiKeyRepository.save(apiKey);

    return new CreateApiKeyResponse(
            apiKey.getId(),
            clientId,
            clientSecret
    );
  }

  private String generateUuid() {
    return UUID.randomUUID().toString();
  }

  /**
   * Delete API keys based on the provided request.
   *
   * <p>This method deletes API keys that match the specified criteria in the request object.
   * It retrieves the API keys by their IDs and company ID,
   * and then deletes them from the database.
   * </p>
   *
   * @param deleteApiKeysRequest the request object containing the
   *                             list of API key IDs and company ID
   */
  @Transactional
  public void deleteApiKeys(DeleteApiKeysRequest deleteApiKeysRequest) {
    List<ApiKey> apiKeys =
            getApiKeysByApiKeyIdsAndCompanyId(
                    deleteApiKeysRequest.getApiKeyIds(),
                    deleteApiKeysRequest.getCompanyId()
            );

    apiKeyRepository.deleteAll(apiKeys);
  }

  /**
   * Update an existing API key.
   *
   * <p>This method updates the details of an existing API key based on the provided request object.
   * It retrieves the API key by its ID and company ID, and then updates its properties.
   * </p>
   *
   * @param id                  the ID of the API key to update
   * @param updateApiKeyRequest the request object containing the updated details for the API key
   * @return the response body containing the updated API key
   * @throws ApiKeyNotFoundException if the API key with the specified ID does not exist
   * @throws ScopeNotFoundException  if any of the specified scopes do not exist
   */
  @Transactional
  public UpdateApiKeyResponse updateApiKey(
          Long id,
          UpdateApiKeyRequest updateApiKeyRequest
  ) throws ApiKeyNotFoundException, ScopeNotFoundException {
    ApiKey apiKey = apiKeyRepository.findByIdAndCompanyId(id, updateApiKeyRequest.getCompanyId())
            .orElseThrow(() -> new ApiKeyNotFoundException("API key not found"));

    List<Scope> scopes = updateApiKeyRequest.getScopes().stream()
            .map(scopeService::getScopeByScopeKey)
            .toList();

    apiKey.setEnabled(updateApiKeyRequest.isEnabled());
    apiKey.setName(updateApiKeyRequest.getName());
    apiKey.setDescription(updateApiKeyRequest.getDescription());
    apiKey.setScopes(scopes);

    apiKeyRepository.save(apiKey);

    return new UpdateApiKeyResponse(
            apiKey.getId(),
            apiKey.getClientId()
    );
  }
}
