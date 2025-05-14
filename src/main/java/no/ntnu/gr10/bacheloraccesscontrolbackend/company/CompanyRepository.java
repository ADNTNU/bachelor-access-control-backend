package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CompanySimpleDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing companies.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    /**
     * Finds a list of CompanySimpleDto objects by the given administrator ID.
     * <p>
     *   This method retrieves a list of companies associated with the specified administrator ID.
     *   The companies are filtered based on their administrator-company relationship and if it is enabled and accepted.
     *   The results are ordered by the company name.
     *   </p>
     *
     * @param administratorId the ID of the administrator
     * @return a list of CompanySimpleDto objects
     */
    @Query("SELECT new no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CompanySimpleDto(" +
                "c.id, " +
                "c.name " +
            ") " +
            "FROM Company c " +
            "JOIN c.administratorCompanies ac " +
            "WHERE ac.id.administratorId = :administratorId " +
            "AND ac.enabled = true " +
            "AND ac.accepted = true " +
            "ORDER BY c.name")
    List<CompanySimpleDto> findCompanySimpleDtosByAdministratorId(Long administratorId);

    /**
     * Finds a company by its name.
     * <p>
     *   This method retrieves a company by its name, ensuring that the associated administrator companies are fetched eagerly.
     *   </p>
     *
     * @param companyName the name of the company
     * @return an Optional containing the found Company or empty if not found
     */
    @EntityGraph(attributePaths = {"administratorCompanies"})
    Optional<Company> findByName(String companyName);

    /**
     * Checks if a company exists by its name.
     * <p>
     *   This method checks if a company with the specified name exists in the database.
     *   </p>
     *
     * @param companyName the name of the company
     * @return true if the company exists, false otherwise
     */
    boolean existsByName(String companyName);
}
