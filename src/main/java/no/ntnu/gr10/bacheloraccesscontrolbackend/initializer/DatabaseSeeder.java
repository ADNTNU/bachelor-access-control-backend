package no.ntnu.gr10.bacheloraccesscontrolbackend.initializer;

import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorRepository;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.AdministratorService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto.InviteAdministratorRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyRepository;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.ScopeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is responsible for seeding the database with initial data.
 */
@Configuration
public class DatabaseSeeder {

  @Bean
  public CommandLineRunner seedDatabase(ScopeRepository scopeRepository,
                                        AdministratorRepository administratorRepository,
                                        CompanyRepository companyRepository, AdministratorService administratorService) {
    return args -> {
      // Create and save Scopes
      if (scopeRepository.count() == 0) {
        Scope scope1 = new Scope("test", "Test Scope", "This is a test scope");
        Scope scope2 = new Scope("ship", "Ship Scope", "This is a ship scope");
        scopeRepository.save(scope1);
        scopeRepository.save(scope2);
      }

      // Create and save Companies
      if (companyRepository.count() == 0) {
        Company company1 = new Company("Company A");
        Company company2 = new Company("Company B");
        companyRepository.saveAndFlush(company1);
        companyRepository.saveAndFlush(company2);
      }

      // Create and save Administrators
      if (administratorRepository.count() == 0) {
        administratorService.addAdministrator(new InviteAdministratorRequest(
                1L, "a_lund_01@hotmail.com", true, "Owner"
        ));
      }
    };
  }
}