package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.CreateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.DeleteApiKeysRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.PaginatedCRUDListRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.UpdateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ApiKeyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api-key")
@Tag(name = "API key", description = "API key endpoints")
public class ApiKeyController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final ApiKeyService apiKeyService;

  @Autowired
  public ApiKeyController(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  @PostMapping("/list")
  public ResponseEntity<?> listApiKeysByCompanyId(@RequestBody PaginatedCRUDListRequest paginatedCRUDListRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(paginatedCRUDListRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      if (paginatedCRUDListRequest.getPage() < 1 || paginatedCRUDListRequest.getSize() <= 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid page or size"));
      }

      return ResponseEntity.ok(
              apiKeyService.getListOfApiKeysByCompanyId(paginatedCRUDListRequest)
      );
    } catch (Exception e) {
      logger.severe("Error fetching API keys: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching API keys"));
    }
  }

  @PostMapping()
  public ResponseEntity<?> createApiKey(@RequestBody CreateApiKeyRequest createApiKeyRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
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
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Scope not found"));
    } catch (Exception e) {
      logger.severe("Error creating API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while creating the API key"));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateApiKey(@PathVariable Long id, @RequestBody UpdateApiKeyRequest updateApiKeyRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
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

  @DeleteMapping()
  public ResponseEntity<?> deleteApiKey(
          @RequestBody DeleteApiKeysRequest deleteApiKeysRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
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
