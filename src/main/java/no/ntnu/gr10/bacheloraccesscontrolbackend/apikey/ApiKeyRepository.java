package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing API keys.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
  Page<ApiKey> findPageableByCompanyIdAndEnabledTrue(long companyId, Pageable pageable);
}
