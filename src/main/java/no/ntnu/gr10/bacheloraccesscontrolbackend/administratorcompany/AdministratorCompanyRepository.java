package no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing AdministratorCompany entities.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 23.04.2025
 */
public interface AdministratorCompanyRepository extends JpaRepository<AdministratorCompany, Long> {

  /**
   * Gets a paginated list of AdministratorCompany entities by company ID.
   *
   * @param companyId the ID of the company
   * @param pageable  the pagination information
   * @return a page of AdministratorCompany entities
   */
  @Query("""
          SELECT DISTINCT ac
          FROM AdministratorCompany ac
          WHERE ac.company.id = :companyId
          """)
  Page<AdministratorCompany> findPageableByCompanyId(long companyId, Pageable pageable);

  /**
   * Gets a list of AdministratorCompany entities by administrator IDs and company ID.
   *
   * @param administratorIds the list of administrator IDs
   * @param companyId        the ID of the company
   * @return a list of AdministratorCompany entities
   */
  @Query("""
          SELECT DISTINCT ac
          FROM AdministratorCompany ac
          WHERE ac.company.id = :companyId
            AND ac.administrator.id IN :administratorIds
          """)
  List<AdministratorCompany> findByAdministratorIdsAndCompanyId(
          List<Long> administratorIds, long companyId
  );

  /**
   * Gets a AdministratorCompany entity by administrator ID and company ID.
   *
   * @param administratorId the ID of the administrator
   * @param companyId       the ID of the company
   * @return an Optional containing the found AdministratorCompany entity, or empty if not found
   */
  Optional<AdministratorCompany> findByAdministratorIdAndCompanyId(
          long administratorId, long companyId
  );

  /**
   * Counts the number of AdministratorCompany entities with enabled and accepted status.
   *
   * @return the count of AdministratorCompany entities
   */
  int countAdministratorCompanyByEnabledIsTrueAndAcceptedIsTrue();
}
