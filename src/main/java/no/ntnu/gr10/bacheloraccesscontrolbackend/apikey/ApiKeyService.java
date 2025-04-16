package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import jakarta.transaction.Transactional;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.*;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ListResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ApiKeyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private final CompanyService companyService;
  private final ScopeService scopeService;

  @Autowired
  public ApiKeyService(ApiKeyRepository apiKeyRepository, CompanyService companyService, ScopeService scopeService) {
    this.apiKeyRepository = apiKeyRepository;
    this.companyService = companyService;
    this.scopeService = scopeService;
  }

  /**
   * Get a list of API keys by company ID with pagination.
   * <p>
   * This method retrieves a list of API keys associated with a specific company ID.
   * It supports pagination by allowing the caller to specify the page number and size.
   * The method returns a paginated list of API keys.
   * </p>
   *
   * @param request the request object containing the company ID, page number, and size
   * @return a paginated list of API keys associated with the specified company ID
   */
  public ListResponse<ApiKeyListDto> getListOfApiKeysByCompanyId(ListApiKeysRequest request) {
    int pageIndex = Math.max(0, request.page() - 1);
    Pageable pageable = PageRequest.of(pageIndex, request.size());

    Page<ApiKey> page = apiKeyRepository.findPageableByCompanyIdAndEnabledTrue(request.companyId(), pageable);

    return new ListResponse<>(
            page.getContent().stream()
                    .map(ApiKeyListDto::new)
                    .toList(),
            page.getTotalPages(),
            page.getTotalElements()
    );
  }

  /**
   * Create a new API key.
   * <p>
   * This method creates a new API key based on the provided request object.
   * It generates a unique API key and saves it to the database.
   * </p>
   *
   * @param createApiKeyRequest the request object containing the details for creating the API key
   * @return the response body containing the created API key
   * @throws CompanyNotFoundException if the company with the specified ID does not exist
   * @throws ScopeNotFoundException if any of the specified scopes do not exist
   */
  @Transactional
  public CreateApiKeyResponse createApiKey(CreateApiKeyRequest createApiKeyRequest) {
    Company company = companyService.getCompanyById(createApiKeyRequest.companyId());

    List<Scope> scopes = createApiKeyRequest.scopes().stream()
            .map(scopeService::getScopeByScopeKey)
            .toList();

    ApiKey apiKey = new ApiKey(
            createApiKeyRequest.enabled(),
            company,
            createApiKeyRequest.name(),
            createApiKeyRequest.description()
    );

    apiKey.setScopes(scopes);

    apiKeyRepository.save(apiKey);

    return new CreateApiKeyResponse(
            apiKey.getId(),
            apiKey.getClientId(),
            apiKey.getClientSecret()
    );
  }

  public void deleteApiKey(Long id) {
    if (!apiKeyRepository.existsById(id)) {
      throw new ApiKeyNotFoundException("API key not found");
    }

    apiKeyRepository.deleteById(id);
  }

  @Transactional
  public UpdateApiKeyResponse updateApiKey(Long id, UpdateApiKeyRequest updateApiKeyRequest) {
    ApiKey apiKey = apiKeyRepository.findById(id)
            .orElseThrow(() -> new ApiKeyNotFoundException("API key not found"));

    if (apiKey.getCompany().getId() != updateApiKeyRequest.companyId()) {
      Company company = companyService.getCompanyById(updateApiKeyRequest.companyId());
      apiKey.setCompany(company);
    }


    List<Scope> scopes = updateApiKeyRequest.scopes().stream()
            .map(scopeService::getScopeByScopeKey)
            .toList();

    apiKey.setEnabled(updateApiKeyRequest.enabled());
    apiKey.setName(updateApiKeyRequest.name());
    apiKey.setDescription(updateApiKeyRequest.description());
    apiKey.setScopes(scopes);

    apiKeyRepository.save(apiKey);

    return new UpdateApiKeyResponse(
            apiKey.getId(),
            apiKey.getClientId()
    );
  }
}
