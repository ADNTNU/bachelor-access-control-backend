package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
  @Query("SELECT a FROM Administrator a WHERE a.username = :input OR a.email = :input")
  Optional<Administrator> findWithAdministratorCompaniesByUsernameOrEmail(@Param("input") String input);

  @Query("SELECT COUNT(a) > 0 FROM Administrator a WHERE a.username = :input OR a.email = :input")
  boolean existsByUsernameOrEmail(@Param("input") String input);

  @Query("SELECT a FROM Administrator a WHERE a.email = :input OR a.username = :input")
  Optional<Administrator> findByUsernameOrEmail(@Param("input") String input);

}
