package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import java.util.List;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CompanySimpleDto;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for getting company-related information.
 * Has methods for CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Service
public class CompanyService {
  final CompanyRepository companyRepository;

  /**
   * Constructor for CompanyService.
   *
   * @param companyRepository the repository for company operations
   */
  @Autowired
  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  /**
   * Get the company by ID.
   *
   * @param id the ID of the company
   * @return the company
   */
  public Company getCompanyById(Long id) throws CompanyNotFoundException {
    return companyRepository.findById(id)
            .orElseThrow(() -> new CompanyNotFoundException("Company not found"));
  }

  /**
   * Get a list of simple company DTOs by administrator ID.
   *
   * @param administratorId the ID of the administrator
   * @return a list of {@link CompanySimpleDto}
   */
  public List<CompanySimpleDto> getSimpleCompaniesByAdministratorId(Long administratorId) {
    return companyRepository.findCompanySimpleDtosByAdministratorId(administratorId);
  }

  /**
   * Creates a new company and saves it to the database.
   *
   * @param company the company to create
   * @return the created company
   */
  public Company createCompany(Company company) {
    return companyRepository.save(company);
  }

  //  TODO: Add methods for creating, updating, and deleting companies
}
