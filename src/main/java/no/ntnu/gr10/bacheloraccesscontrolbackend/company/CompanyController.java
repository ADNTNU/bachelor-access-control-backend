package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * Controller for handling company-related requests.
 * Includes methods for fetching company information.
 *
 * @author Anders Lund
 * @version 08.04.2025
 */
@RestController
@RequestMapping("/company")
@Tag(name = "Company", description = "Company endpoints")
public class CompanyController {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  /**
   * Fetches all companies associated with the authenticated user.
   */
  @GetMapping("/all")
  public ResponseEntity<?> getAllCompanies(@AuthenticationPrincipal CustomUserDetails userDetails) {
    try {

    return ResponseEntity.ok(
            companyService.getSimpleCompaniesByAdministratorId(userDetails.getId())
    );
    } catch (Exception e) {
      logger.severe("Error fetching companies: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching companies"));
    }
  }
}
