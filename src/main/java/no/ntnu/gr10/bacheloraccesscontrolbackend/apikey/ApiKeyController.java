package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.logging.Logger;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.CreateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.DeleteApiKeysRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.UpdateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.PaginatedCrudListRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ApiKeyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller class for managing API keys.
 * Provides endpoints for creating, updating, deleting, and listing API keys.
 *
 * @author Anders Lund
 * @version 16.04.2025
 */
@RestController
@RequestMapping("/api-key")
@Tag(name = "API key", description = "API key endpoints")
public class ApiKeyController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final ApiKeyService apiKeyService;

  /**
   * Constructor for ApiKeyController.
   *
   * @param apiKeyService the service for managing API keys
   */
  @Autowired
  public ApiKeyController(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  /**
   * Endpoint to list API keys by company ID.
   *
   * @param paginatedCrudListRequest the request object containing pagination and company ID
   * @param userDetails              the authenticated user details
   * @return a ResponseEntity containing the list of API keys with pagination details or
   *         an error response
   */
  @PostMapping("/list")
  public ResponseEntity<?> listApiKeysByCompanyId(
          @RequestBody PaginatedCrudListRequest paginatedCrudListRequest,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    try {
      if (!userDetails.getCompanyIds().contains(paginatedCrudListRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      if (paginatedCrudListRequest.getPage() < 1 || paginatedCrudListRequest.getSize() <= 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid page or size"));
      }

      return ResponseEntity.ok(
              apiKeyService.getListOfApiKeysByCompanyId(paginatedCrudListRequest)
      );
    } catch (Exception e) {
      logger.severe("Error fetching API keys: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching API keys"));
    }
  }

  /**
   * Endpoint to create a new API key.
   *
   * @param createApiKeyRequest the request object containing API key details
   * @param userDetails         the authenticated user details
   * @return a ResponseEntity containing the created API key or an error response
   */
  @PostMapping()
  public ResponseEntity<?> createApiKey(
          @RequestBody CreateApiKeyRequest createApiKeyRequest,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    try {
      if (!userDetails.getCompanyIds().contains(createApiKeyRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }


      return ResponseEntity.ok(
              apiKeyService.createApiKey(createApiKeyRequest)
      );
    } catch (CompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Company not found"));
    } catch (ScopeNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(new ErrorResponse("Scope not found"));
    } catch (Exception e) {
      logger.severe("Error creating API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while creating the API key"));
    }
  }

  /**
   * Endpoint to update an existing API key.
   *
   * @param id                  the ID of the API key to update
   * @param updateApiKeyRequest the request object containing updated API key details
   * @param userDetails         the authenticated user details
   * @return a ResponseEntity containing the updated API key or an error response
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> updateApiKey(
          @PathVariable Long id,
          @RequestBody UpdateApiKeyRequest updateApiKeyRequest,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    try {
      if (!userDetails.getCompanyIds().contains(updateApiKeyRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      return ResponseEntity.ok(
              apiKeyService.updateApiKey(id, updateApiKeyRequest)
      );
    } catch (ApiKeyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("API key not found"));
    } catch (CompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Company not found"));
    } catch (ScopeNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Scope not found"));
    } catch (Exception e) {
      logger.severe("Error updating API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while updating the API key"));
    }
  }

  /**
   * Endpoint to delete API keys.
   *
   * @param deleteApiKeysRequest the request object containing API key IDs and company ID
   * @param userDetails          the authenticated user details
   * @return a ResponseEntity indicating the result of the deletion operation
   */
  @DeleteMapping()
  public ResponseEntity<?> deleteApiKey(
          @RequestBody DeleteApiKeysRequest deleteApiKeysRequest,
          @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    try {
      if (!userDetails.getCompanyIds().contains(deleteApiKeysRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      apiKeyService.deleteApiKeys(deleteApiKeysRequest);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ApiKeyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("API key not found"));
    } catch (Exception e) {
      logger.severe("Error deleting API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while deleting the API key"));
    }
  }
}
