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

    @EntityGraph(attributePaths = {"administratorCompanies"})
    Optional<Company> findByName(String companyA);
}
