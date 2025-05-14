package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing scopes.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 06.04.2025
 */
public interface ScopeRepository extends JpaRepository<Scope, Long> {

  /**
   * Find all scopes that are enabled.
   *
   * @return A list of enabled scopes as ScopeSimpleDto.
   */
  @Query("SELECT new no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto(" +
            "s.key, " +
            "s.name, " +
            "s.description" +
          ") " +
          "FROM Scope s " +
          "WHERE s.enabled = true")
  List<ScopeSimpleDto> findAllScopeSimpleDtosByEnabledIsTrue();

  /**
   * Find a scope by its key.
   *
   * @param key The key of the scope to find.
   * @return An Optional containing the found scope, or empty if not found.
   */
  Optional<Scope> findDistinctByKeyIsAndEnabledIsTrue(String key);

  /**
   * Check if a scope with the given key exists in the database.
   *
   * @param key The key of the scope to check.
   * @return true if the scope exists, false otherwise.
   */
  boolean existsByKey(String key);
}
