package no.ntnu.gr10.bacheloraccesscontrolbackend.repositories;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

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
  Optional<ApiKey> findByClientIdAndClientSecret(String clientId, String clientSecret);

  List<ApiKey> findApiKeysByCompanyId(long companyId, org.springframework.data.domain.Pageable pageable);
}
