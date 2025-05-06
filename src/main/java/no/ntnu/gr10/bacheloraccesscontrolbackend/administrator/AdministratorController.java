package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto.*;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.PasswordPolicyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.SuccessResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.PaginatedCRUDListRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.AdministratorCompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.InvalidRoleException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.SendMailException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/administrator")
@Tag(name = "Administrator", description = "Administrator endpoints")
public class AdministratorController {

  Logger logger = Logger.getLogger(getClass().getName());

  private final AdministratorService administratorService;

  @Autowired
  public AdministratorController(AdministratorService administratorService) {
    this.administratorService = administratorService;
  }

  @PostMapping("/list")
  public ResponseEntity<?> listAdministratorsByCompanyId(@RequestBody PaginatedCRUDListRequest paginatedCRUDListRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
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
              administratorService.getListOfAdministratorsByCompanyId(paginatedCRUDListRequest)
      );
    } catch (Exception e) {
      logger.severe("Error fetching administrator: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching administrators"));
    }
  }

  @PostMapping("/invite")
  public ResponseEntity<?> inviteAdministrator(@RequestBody InviteAdministratorRequest inviteAdministratorRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(inviteAdministratorRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      administratorService.inviteAdministrator(inviteAdministratorRequest);

      return ResponseEntity.status(HttpStatus.CREATED)
              .body(new SuccessResponse("Invitation sent successfully"));
    } catch (CompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Company not found"));
    } catch (InvalidRoleException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Role not found"));
    } catch (InvalidKeyException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Invalid token"));
    } catch (SendMailException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("Error sending email"));
    } catch (Exception e) {
      logger.severe("Error inviting administrator: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while trying to invite the administrator"));
    }
  }

  @PostMapping("/register-from-invite")
  public ResponseEntity<?> registerAndAcceptInvite(@RequestBody RegisterAndAcceptAdministratorInviteRequest acceptAdministratorInviteRequest) {
    try {
      administratorService.registerAdministratorAndAcceptInvite(acceptAdministratorInviteRequest);

      return ResponseEntity.status(HttpStatus.OK)
              .body(new SuccessResponse("Invitation accepted successfully"));
    } catch (JwtException | AdministratorCompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new ErrorResponse("Invalid token"));
    } catch (PasswordPolicyService.WeakPasswordException | IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(new ErrorResponse("Invalid registration data"));
    } catch (Exception e) {
      logger.severe("Error registering and accepting invite: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while processing the request"));
    }
  }

  @PostMapping("/accept-invite")
  public ResponseEntity<?> acceptInvite(@RequestBody AcceptAdministratorInviteRequest acceptAdministratorInviteRequest) {
    try {
      administratorService.acceptInvite(acceptAdministratorInviteRequest);

      return ResponseEntity.status(HttpStatus.OK)
              .body(new ErrorResponse("Invitation accepted successfully"));

    } catch (JwtException | AdministratorCompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new ErrorResponse("Invalid token"));
    } catch (Exception e) {
      logger.severe("Error accepting invite: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while accepting the invitation"));
    }
  }

  @PutMapping("/{administratorId}")
  public ResponseEntity<?> updateAdministratorCompany(@PathVariable("administratorId") Long administratorId,
                                                      @RequestBody UpdateAdministratorCompanyRequest updateAdministratorCompanyRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(updateAdministratorCompanyRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      administratorService.updateAdministratorCompany(updateAdministratorCompanyRequest, administratorId);

      return ResponseEntity.status(HttpStatus.OK)
              .body(new ErrorResponse("Administrator updated successfully"));
    } catch (AdministratorCompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Administrator not found"));
    } catch (CompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Company not found"));
    } catch (InvalidRoleException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(new ErrorResponse("Invalid role"));
    } catch (Exception e) {
      logger.severe("Error updating AdministratorCompany: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while updating the administrator"));
    }
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteAdministratorCompany(
                                                      @RequestBody DeleteAdministratorCompanyRequest deleteAdministratorCompanyRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      if (!userDetails.getCompanyIds().contains(deleteAdministratorCompanyRequest.getCompanyId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("You do not have access to this company"));
      }

      administratorService.deleteAdministratorCompany(deleteAdministratorCompanyRequest);

      return ResponseEntity.status(HttpStatus.OK)
              .body(new ErrorResponse("Administrator successfully removed from company"));
    } catch (AdministratorCompanyNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new ErrorResponse("Administrator not found"));
    } catch (Exception e) {
      logger.severe("Error deleting AdministratorCompany: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while deleting the administrator"));
    }
  }
}
