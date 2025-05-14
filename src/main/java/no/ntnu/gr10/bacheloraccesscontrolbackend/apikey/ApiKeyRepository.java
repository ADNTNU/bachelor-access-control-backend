package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing API keys.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
  /**
   * Find a paginated list of API keys by company ID.
   * @param companyId the ID of the company
   * @param pageable the pagination information
   * @return a paginated list of API keys associated with the specified company ID
   */
  Page<ApiKey> findPageableByCompanyId(long companyId, Pageable pageable);

  /**
   * Find an API key by its ID and company ID.
   * @param id the ID of the API key
   * @param companyId the ID of the company
   * @return an Optional containing the found API key, or empty if not found
   */
  Optional<ApiKey> findByIdAndCompanyId(Long id, Long companyId);

  /**
   * Get a list of API keys by company ID and a list of API key IDs.
   * @param apiKeyIds the list of API key IDs to search for
   * @param companyId the company ID to filter by
   * @return a list of API keys that match the given IDs and company ID
   */
  @Query("""
    SELECT DISTINCT apiKey
    FROM ApiKey apiKey
    WHERE apiKey.company.id = :companyId
      AND apiKey.id IN :apiKeyIds
  """)
  List<ApiKey> findByApiKeyIdsAndCompanyId(List<Long> apiKeyIds, long companyId);
}
