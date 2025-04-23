package no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdministratorCompanyRepository extends JpaRepository<AdministratorCompany, Long> {

  @Query("""
    SELECT DISTINCT ac
    FROM AdministratorCompany ac
    WHERE ac.company.id = :companyId
""")
  Page<AdministratorCompany> findPageableByCompanyId(long companyId, Pageable pageable);

  @Query("""
    SELECT DISTINCT ac
    FROM AdministratorCompany ac
    WHERE ac.company.id = :companyId
      AND ac.administrator.id IN :administratorIds
  """)
  List<AdministratorCompany> findByAdministratorIdsAndCompanyId(List<Long> administratorIds, long companyId);

  Optional<AdministratorCompany> findByAdministratorIdAndCompanyId(long administratorId, long companyId);
}
