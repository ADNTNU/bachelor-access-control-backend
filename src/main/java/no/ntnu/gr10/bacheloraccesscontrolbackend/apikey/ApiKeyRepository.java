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
  Page<ApiKey> findPageableByCompanyId(long companyId, Pageable pageable);

  Optional<ApiKey> findByIdAndCompanyId(Long id, Long companyId);

  @Query("""
    SELECT DISTINCT apiKey
    FROM ApiKey apiKey
    WHERE apiKey.company.id = :companyId
      AND apiKey.id IN :apiKeyIds
  """)
  List<ApiKey> findByApiKeyIdsAndCompanyId(List<Long> apiKeyIds, long companyId);
}
