package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.CreateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.ListApiKeysRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto.UpdateApiKeyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ApiKeyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.ScopeNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-key")
@Tag(name = "API key", description = "API key endpoints")
public class ApiKeyController {


  private final ApiKeyService apiKeyService;

  @Autowired
  public ApiKeyController(ApiKeyService apiKeyService, CompanyService companyService) {
    this.apiKeyService = apiKeyService;
  }

  @PostMapping("/list")
  public ResponseEntity<?> listApiKeysByCompanyId(@RequestBody ListApiKeysRequest listApiKeysRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(listApiKeysRequest.companyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      if (listApiKeysRequest.page() < 1 || listApiKeysRequest.size() <= 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid page or size"));
      }

      return ResponseEntity.ok(
              apiKeyService.getListOfApiKeysByCompanyId(listApiKeysRequest)
      );
    } catch (Exception e) {
      System.err.println("Error fetching API keys: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching API keys"));
    }
  }

  @PostMapping()
  public ResponseEntity<?> createApiKey(@RequestBody CreateApiKeyRequest createApiKeyRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(createApiKeyRequest.companyId())) {
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
      System.err.println("Error creating API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while creating the API key"));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateApiKey(@PathVariable Long id, @RequestBody UpdateApiKeyRequest updateApiKeyRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(updateApiKeyRequest.companyId())) {
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
      System.err.println("Error updating API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while updating the API key"));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteApiKey(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {
      apiKeyService.deleteApiKey(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ApiKeyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("API key not found"));
    } catch (Exception e) {
      System.err.println("Error deleting API key: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while deleting the API key"));
    }
  }
}
