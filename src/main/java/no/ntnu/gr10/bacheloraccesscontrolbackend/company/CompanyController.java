package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CreateCompanyRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CompanySimpleDto;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.UserDetailsImpl;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

  private final CompanyService companyService;
  private final AdministratorService administratorService;

  @Autowired
  public CompanyController(CompanyService companyService, AdministratorService administratorService) {
    this.companyService = companyService;
    this.administratorService = administratorService;
  }

  /**
   * Fetches all companies associated with the authenticated user.
   */
  @GetMapping("/all")
  public ResponseEntity<?> getAllCompanies(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {

    return ResponseEntity.ok(
            companyService.getSimpleCompaniesByAdministratorId(userDetails.getId())
    );
    } catch (Exception e) {
      System.err.println("Error fetching companies: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while fetching companies"));
    }
  }

//  TODO: Remove the temporary endpoint for creating a company
//   when a proper solution is in place
  /**
   * Temporary endpoint to create a company.
   */
  @PostMapping("/create")
  public ResponseEntity<?> createCompany(@RequestBody CreateCompanyRequest request) {
    try {
      Company company = new Company(
              request.name()
      );
      Administrator administrator = administratorService.getByUsername(request.administratorUsername());
      company.addAdministrator(administrator);
      Company newCompany = companyService.createCompany(company);
      return ResponseEntity.ok(new CompanySimpleDto(newCompany.getId(), newCompany.getName()));
    } catch (Exception e) {
      System.err.println("Error creating company: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse("An error occurred while creating the company"));
    }
  }

}
