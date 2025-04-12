package no.ntnu.gr10.bacheloraccesscontrolbackend.company;

import no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto.CompanySimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
            "JOIN c.administrators a " +
            "WHERE a.id = :administratorId " +
            "ORDER BY c.name")
    List<CompanySimpleDto> findCompanySimpleDtosByAdministratorId(Long administratorId);
}
