package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.logging.Logger;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling scope-related requests.
 * Includes methods for fetching scope information.
 *
 * @author Anders Lund
 * @version 11.04.2025
 */
@RestController
@RequestMapping("/scope")
@Tag(name = "Scope", description = "Scope endpoints")
public class ScopeController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final ScopeService scopeService;

  /**
   * Constructor for ScopeController.
   *
   * @param scopeService the service for managing scopes
   */
  @Autowired
  public ScopeController(ScopeService scopeService) {
    this.scopeService = scopeService;
  }

  /**
   * Fetches all enabled scopes.
   * This endpoint returns a list of all scopes that are currently enabled in the system.
   *
   * @return ResponseEntity containing the list of enabled scopes or an error message
   */
  @GetMapping("/all")
  public ResponseEntity<?> getAllScopes() {
    try {
      return ResponseEntity.ok(scopeService.getAllEnabledSimpleScopes());
    } catch (Exception e) {
      logger.severe("Error fetching scopes: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching scopes"));
    }
  }

}