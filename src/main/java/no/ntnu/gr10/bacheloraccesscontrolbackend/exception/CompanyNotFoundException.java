package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;


import jakarta.persistence.EntityNotFoundException;

/**
 * Custom exception class for handling company not found errors.
 * This class extends EntityNotFoundException and is used to indicate
 * that a specific company was not found in the system.
 *
 * @author Anders Lund
 * @version 14.04.2025
 */
@SuppressWarnings("squid:S110")
public class CompanyNotFoundException extends EntityNotFoundException {
    public CompanyNotFoundException(String message) {
      super(message);
    }
}
