package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.logging.Logger;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  /**
   * Constructor for CompanyController.
   *
   * @param companyService the service for handling company-related operations
   */
  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  /**
   * Endpoint to fetch all companies associated with the authenticated user.
   *
   * @param userDetails the authenticated user details
   * @return a ResponseEntity containing the list of companies or an error response
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
