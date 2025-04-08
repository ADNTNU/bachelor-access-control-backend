package no.ntnu.gr10.bacheloraccesscontrolbackend.repositories;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing scopes.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 06.04.2025
 */
public interface ScopeRepository extends JpaRepository<Scope, Long> {

}
