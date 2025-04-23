package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.dto.ScopeSimpleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

  @Autowired
  public ScopeService(ScopeRepository scopeRepository) {
    this.scopeRepository = scopeRepository;
  }

  public List<ScopeSimpleDto> getAllSimpleScopes() {
    return scopeRepository.findAllScopeSimpleDtos();
  }

  public Scope getScopeByScopeKey(String scopeKey) {
    return scopeRepository.findByKey(scopeKey)
            .orElseThrow(() -> new ScopeNotFoundException("Scope not found"));

  }

//  TODO: Add methods for creating, updating, and deleting scopes
}
