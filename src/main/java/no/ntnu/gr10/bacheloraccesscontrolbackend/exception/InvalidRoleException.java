package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;

/**
 * Custom exception class for handling invalid role errors.
 * This class extends IllegalArgumentException and is used to indicate
 * that a specific role is invalid in the system.
 *
 * @author Anders Lund
 * @version 16.04.2025
 */
public class InvalidRoleException extends IllegalArgumentException {
  /**
   * Default constructor for InvalidRoleException.
   *
   * @param message the error message to be associated with this exception
   */
  public InvalidRoleException(String message) {
    super(message);
  }
}