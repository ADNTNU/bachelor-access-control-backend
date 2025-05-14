package no.ntnu.gr10.bacheloraccesscontrolbackend.auth;

import org.springframework.stereotype.Service;

/**
 * Service for validating password policies.
 * This service checks if the password meets certain criteria such as length, presence of numbers,
 * uppercase letters, etc.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
@Service
  public class PasswordPolicyService {

    public void validatePassword(String password) throws WeakPasswordException {
      if (password.length() < 12) {
        throw new WeakPasswordException("Password must be at least 12 characters long");
      }
      if (!password.matches(".*\\d.*")) {
        throw new WeakPasswordException("Password must contain at least one number");
      }
      if (!password.matches(".*[A-Z].*")) {
        throw new WeakPasswordException("Password must contain at least one uppercase letter");
      }
      // Add more checks if needed
    }

    /**
     * Exception thrown when a password does not meet the policy requirements.
     *
     * @author Anders Lund
     * @version 23.04.2025
     */
    public static class WeakPasswordException extends RuntimeException {
        public WeakPasswordException(String message) {
            super(message);
        }

        public WeakPasswordException(String message, Throwable cause) {
            super(message, cause);
        }
  }
}
