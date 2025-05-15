package no.ntnu.gr10.bacheloraccesscontrolbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Bachelor Access Control Backend.
 * This class serves as the entry point for the Spring Boot application.
 * It is responsible for bootstrapping the application and initializing the Spring context.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@SpringBootApplication
public class BachelorAccessControlBackendApplication {

  /**
   * Main method to run the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(BachelorAccessControlBackendApplication.class, args);
  }

}
