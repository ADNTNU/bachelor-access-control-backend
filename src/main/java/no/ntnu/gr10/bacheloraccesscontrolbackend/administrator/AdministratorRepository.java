package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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

  boolean existsByUsername(String username);

  @EntityGraph(attributePaths = {"administratorCompanies"})
  Optional<Administrator> findWithAdministratorCompaniesByUsername(String username);
}
