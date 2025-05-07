package no.ntnu.gr10.bacheloraccesscontrolbackend.initializer;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.Administrator;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorRepository;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorRole;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyRepository;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.ScopeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * This class is responsible for seeding the database with initial data.
 */
@Configuration
public class DatabaseSeeder {

  @Bean
  public CommandLineRunner seedDatabase(ScopeRepository scopeRepository,
                                        AdministratorRepository administratorRepository,
                                        CompanyRepository companyRepository, AdministratorService administratorService, PasswordEncoder passwordEncoder) {
    return args -> {
      // Create and save Scopes
      if (!scopeRepository.existsByKey("fishery-activity")) {
        Scope scope = new Scope("fishery-activity", "Fishery activity", "Allows reading fishery activity data");
        scopeRepository.save(scope);
      }
      if (!scopeRepository.existsByKey("fishing-facility")) {
        Scope scope = new Scope("fishing-facility", "Fishery facility", "Allows reading fishery facility data");
        scopeRepository.save(scope);
      }

      Company companyForAdministrator;

      // Create and save Companies
      if (!companyRepository.existsByName("Company A")) {
        Company company1 = new Company("Company A");
        companyForAdministrator = companyRepository.save(company1);
      } else {
        companyForAdministrator = companyRepository.findByName("Company A").orElseThrow();
      }

      if (!companyRepository.existsByName("Company B")) {
        Company company2 = new Company("Company B");
        companyRepository.save(company2);
      }

      // Create and save Administrators
      String adminEmail = "a_lund_01@hotmail.com";
      @SuppressWarnings("squid:S6437")
      String encodedPassword = passwordEncoder.encode("Test12345678");
      if (!administratorRepository.existsByUsernameOrEmail(adminEmail)) {
        Administrator administrator = new Administrator(
                adminEmail,
                "AndersL01",
                encodedPassword,
                "Anders",
                "Lund");
        administrator.setRegistered(new Date());
        administratorService.addAdministratorToCompany(companyForAdministrator.getId(),
                administrator, AdministratorRole.OWNER
        );
      }
    };
  }
}