package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;


import jakarta.persistence.EntityNotFoundException;

/**
 * Custom exception class for handling administratorCompany not found errors.
 * This class extends EntityNotFoundException and is used to indicate
 * that a specific administrator-company link was not found in the system.
 *
 * @author Anders Lund
 * @version 19.04.2025
 */
@SuppressWarnings("squid:S110")
public class AdministratorCompanyNotFoundException extends EntityNotFoundException {
    public AdministratorCompanyNotFoundException(String message) {
      super(message);
    }
}
