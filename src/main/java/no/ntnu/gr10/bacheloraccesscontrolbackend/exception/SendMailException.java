package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;

/**
 * Custom exception class for handling email sending errors.
 * This class extends RuntimeException and is used to indicate
 * issues that occur during the process of sending emails.
 *
 * @author Anders Lund
 * @version 18.04.2025
 */
public class SendMailException extends RuntimeException {
  public SendMailException(String message) {
    super(message);
  }

  public SendMailException(String message, Throwable cause) {
    super(message, cause);
  }
}
