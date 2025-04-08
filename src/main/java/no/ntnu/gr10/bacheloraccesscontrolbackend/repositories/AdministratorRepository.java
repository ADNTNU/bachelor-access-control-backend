package no.ntnu.gr10.bacheloraccesscontrolbackend.repositories;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing administrators.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
  Optional<Administrator> findByUsername(String username);

  @Query("SELECT a FROM Administrator a JOIN a.companies c WHERE c.id = :companyId")
  List<Administrator> findAdministratorsByCompanyId(long companyId, org.springframework.data.domain.Pageable pageable);

  boolean existsByUsername(String username);
}
