package no.ntnu.gr10.bacheloraccesscontrolbackend.auth;

import org.springframework.stereotype.Service;

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

    public static class WeakPasswordException extends RuntimeException {
        public WeakPasswordException(String message) {
            super(message);
        }

        public WeakPasswordException(String message, Throwable cause) {
            super(message, cause);
        }
  }
}
