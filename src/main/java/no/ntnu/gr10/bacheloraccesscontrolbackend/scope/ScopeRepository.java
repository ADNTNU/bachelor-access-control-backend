package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing scopes.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 06.04.2025
 */
public interface ScopeRepository extends JpaRepository<Scope, Long> {

  @Query("SELECT new no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto(" +
            "s.id, " +
            "s.name, " +
            "s.description" +
          ") " +
          "FROM Scope s " +
          "WHERE s.enabled = true")
  List<ScopeSimpleDto> findAllScopeSimpleDtos();
}
