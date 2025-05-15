package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import java.util.List;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service class for managing scopes.
 * Provides methods for CRUD operations on scopes.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
@Service
public class ScopeService {

  private final ScopeRepository scopeRepository;

  /**
   * Constructor for ScopeService.
   *
   * @param scopeRepository the repository for accessing scope data.
   */
  @Autowired
  public ScopeService(ScopeRepository scopeRepository) {
    this.scopeRepository = scopeRepository;
  }

  /**
   * Retrieves all scopes that are enabled.
   *
   * @return a list of enabled scopes
   */
  public List<ScopeSimpleDto> getAllEnabledSimpleScopes() {
    return scopeRepository.findAllScopeSimpleDtosByEnabledIsTrue();
  }

  /**
   * Retrieves a scope by its key.
   *
   * @param scopeKey the key of the scope to retrieve
   * @return the scope with the specified key
   * @throws ScopeNotFoundException if no scope with the specified key is found
   */
  public Scope getScopeByScopeKey(String scopeKey) throws ScopeNotFoundException {
    return scopeRepository.findDistinctByKeyIsAndEnabledIsTrue(scopeKey)
            .orElseThrow(() -> new ScopeNotFoundException("Scope not found"));

  }

  //  TODO: Add methods for creating, updating, and deleting scopes
}
