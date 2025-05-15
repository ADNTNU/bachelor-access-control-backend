package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Custom exception class for handling scope not found errors.
 * This class extends EntityNotFoundException and is used to indicate
 * that a specific scope was not found in the system.
 *
 * @author Anders Lund
 * @version 16.04.2025
 */
@SuppressWarnings("squid:S110")
public class ScopeNotFoundException extends EntityNotFoundException {
  /**
   * Default constructor for ScopeNotFoundException.
   *
   * @param message the error message to be associated with this exception
   */
  public ScopeNotFoundException(String message) {
    super(message);
  }
}
