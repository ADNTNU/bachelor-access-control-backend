package no.ntnu.gr10.bacheloraccesscontrolbackend.scope;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

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

  @Autowired
  public ScopeController(ScopeService scopeService) {
    this.scopeService = scopeService;
  }

  /**
   *
   */
  @GetMapping("/all")
  public ResponseEntity<?> getAllScopes() {
    try {
      return ResponseEntity.ok(scopeService.getAllSimpleScopes());
    } catch (Exception e) {
      logger.severe("Error fetching scopes: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred while fetching scopes"));
    }
  }

}