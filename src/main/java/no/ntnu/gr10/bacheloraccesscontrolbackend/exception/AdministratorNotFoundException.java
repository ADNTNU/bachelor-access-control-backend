package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;


import jakarta.persistence.EntityNotFoundException;

/**
 * Custom exception class for handling administrator not found errors.
 * This class extends EntityNotFoundException and is used to indicate
 * that a specific administrator was not found in the system.
 *
 * @author Anders Lund
 * @version 17.04.2025
 */
@SuppressWarnings("squid:S110")
public class AdministratorNotFoundException extends EntityNotFoundException {
  /**
   * Default constructor for AdministratorNotFoundException.
   *
   * @param message the detail message
   */
  public AdministratorNotFoundException(String message) {
    super(message);
  }
}
