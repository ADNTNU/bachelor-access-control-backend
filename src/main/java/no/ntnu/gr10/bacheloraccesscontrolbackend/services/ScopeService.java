package no.ntnu.gr10.bacheloraccesscontrolbackend.services;

import no.ntnu.gr10.bacheloraccesscontrolbackend.entities.Scope;
import no.ntnu.gr10.bacheloraccesscontrolbackend.repositories.ScopeRepository;
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

  /**
   * Get a list of all scopes.
   */
  public List<Scope> getAllScopes() {
    return scopeRepository.findAll();
  }

//  TODO: Add methods for creating, updating, and deleting scopes
}
