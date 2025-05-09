package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query("SELECT new no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto(" +
            "s.key, " +
            "s.name, " +
            "s.description" +
          ") " +
          "FROM Scope s " +
          "WHERE s.enabled = true")
  List<ScopeSimpleDto> findAllScopeSimpleDtos();

  @Query("""
          SELECT s
          FROM Scope s
          WHERE s.key = :scopeKey AND s.enabled = true
""")
  Optional<Scope> findByKey(@Param("scopeKey") String scopeKey);

  boolean existsByKey(String key);
}
