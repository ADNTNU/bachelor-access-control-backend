package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.transaction.Transactional;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administrator.dto.*;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompanyRepository;
import no.ntnu.gr10.bacheloraccesscontrolbackend.auth.PasswordPolicyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.CompanyService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.ListResponse;
import no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests.PaginatedCRUDListRequest;
import no.ntnu.gr10.bacheloraccesscontrolbackend.email.SendGridService;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.AdministratorCompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.CompanyNotFoundException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.InvalidRoleException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.exception.SendMailException;
import no.ntnu.gr10.bacheloraccesscontrolbackend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing administrators.
 * Provides methods for CRUD operations as well as for authentication.
 *
 * @author Anders Lund
 * @version 05.04.2025
 */
@Service
public class AdministratorService {

  private final AdministratorRepository administratorRepository;
  private final AdministratorCompanyRepository administratorCompanyRepository;
  private final PasswordEncoder passwordEncoder;
  private final CompanyService companyService;
  private final SendGridService sendGridService;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordPolicyService passwordPolicyService;

  @Value("${frontend.base.url}")
  private String frontendBaseUrl;

  @Autowired
  public AdministratorService(AdministratorRepository administratorRepository,
                              AdministratorCompanyRepository administratorCompanyRepository,
                              PasswordEncoder passwordEncoder, CompanyService companyService,
                              SendGridService sendGridService, JwtTokenProvider jwtTokenProvider, PasswordPolicyService passwordPolicyService) {
    this.administratorRepository = administratorRepository;
    this.administratorCompanyRepository = administratorCompanyRepository;
    this.passwordEncoder = passwordEncoder;
    this.companyService = companyService;
    this.sendGridService = sendGridService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordPolicyService = passwordPolicyService;
  }

  /**
   * Get an Administrator by username.
   * <p>
   * This method retrieves an Administrator entity from the database based on the provided username.
   * If the Administrator is not found, it throws a UsernameNotFoundException.
   * </p>
   *
   * @param username the username of the Administrator to be retrieved
   * @return the found {@link Administrator} entity
   * @throws UsernameNotFoundException if the Administrator is not found
   */
  public Administrator getByUsername(String username) {
    return administratorRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }


  public Administrator getById(long adminId) {
    return administratorRepository.findById(adminId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public Administrator getByUsernameOrEmail(String usernameOrEmail) {
    return administratorRepository.findByUsernameOrEmail(usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public List<AdministratorCompany> getAdministratorCompaniesByAdministratorIdsAndCompanyId(List<Long> administratorIds, long companyId) {
    return administratorCompanyRepository.findByAdministratorIdsAndCompanyId(administratorIds, companyId);
  }

  public AdministratorCompany getAdministratorCompanyByAdministratorIdAndCompanyId(long administratorId, long companyId) throws AdministratorCompanyNotFoundException {
    return administratorCompanyRepository.findByAdministratorIdAndCompanyId(administratorId, companyId)
            .orElseThrow(() -> new AdministratorCompanyNotFoundException("AdministratorCompany not found"));
  }

  public ListResponse<AdministratorListDto> getListOfAdministratorsByCompanyId(PaginatedCRUDListRequest request) {
    int pageIndex = Math.max(0, request.getPage() - 1);
    Pageable pageable = PageRequest.of(pageIndex, request.getSize());

    Page<AdministratorCompany> page = administratorCompanyRepository.findPageableByCompanyId(request.getCompanyId(), pageable);

    return new ListResponse<>(
            page.getContent().stream()
                    .map(AdministratorListDto::new)
                    .toList(),
            page.getTotalPages(),
            page.getTotalElements()
    );
  }

  @Transactional
  public void inviteAdministrator(InviteAdministratorRequest inviteAdministratorRequest) throws CompanyNotFoundException, InvalidRoleException, InvalidKeyException, SendMailException {
    Company company = companyService.getCompanyById(inviteAdministratorRequest.getCompanyId());

    @SuppressWarnings("squid:S6437")
    String encodedPassword = passwordEncoder.encode("temporaryPassword");

    String temporaryUUIDForUsername = UUID.randomUUID().toString();

    Administrator administrator = administratorRepository.findByEmail(inviteAdministratorRequest.getEmail()).orElse(
            new Administrator(
                    inviteAdministratorRequest.getEmail(),
                    temporaryUUIDForUsername,
                    encodedPassword, // Temporary password before user sets up the account
                    "temporaryFirstName", // Temporary first name before user sets up the account
                    "temporaryLastName" // Temporary last name before user sets up the account
            )
    );

    AdministratorRole role = AdministratorRole.fromString(inviteAdministratorRequest.getRole());
    administrator.addCompanyWithRole(
            company,
            role
    );

    Administrator newAdmin = administratorRepository.save(administrator);

    String token = jwtTokenProvider.generateInviteToken(String.valueOf(newAdmin.getId()), String.valueOf(company.getId()), administrator.isRegistered(), company.getName());
    String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
    String inviteLink = String.format("%s/accept-invite?token=%s", frontendBaseUrl, encodedToken);

    sendGridService.sendInviteAdminEmail(
            inviteAdministratorRequest.getEmail(),
            company.getName(),
            inviteLink
    );
  }

  @Transactional
  public void acceptInvite(AcceptAdministratorInviteRequest acceptAdministratorInviteRequest) throws AdministratorCompanyNotFoundException, JwtException, IllegalStateException {
    AdministratorCompany administratorCompany = getAdministratorCompanyFromInviteToken(acceptAdministratorInviteRequest.getInviteToken());

    Administrator administrator = administratorCompany.getAdministrator();

    if (!administrator.isRegistered()) {
      throw new IllegalStateException("Administrator is not registered");
    }

    administratorCompany.setEnabled(true);
    administratorCompany.setAccepted(true);

    administratorCompanyRepository.save(administratorCompany);
  }

  @Transactional
  public void registerAdministratorAndAcceptInvite(RegisterAndAcceptAdministratorInviteRequest registerAndAcceptAdministratorInviteRequest) throws AdministratorCompanyNotFoundException, JwtException, PasswordPolicyService.WeakPasswordException, IllegalArgumentException {
    AdministratorCompany administratorCompany = getAdministratorCompanyFromInviteToken(registerAndAcceptAdministratorInviteRequest.getInviteToken());

    Administrator administrator = administratorCompany.getAdministrator();

    administratorCompany.setAccepted(true);

    passwordPolicyService.validatePassword(registerAndAcceptAdministratorInviteRequest.getPassword());
    String encodedPassword = passwordEncoder.encode(registerAndAcceptAdministratorInviteRequest.getPassword());
    administrator.setUsername(registerAndAcceptAdministratorInviteRequest.getUsername());
    administrator.setPassword(encodedPassword);
    administrator.setFirstName(registerAndAcceptAdministratorInviteRequest.getFirstName());
    administrator.setLastName(registerAndAcceptAdministratorInviteRequest.getLastName());
    administrator.setRegistered(new Date());

    administratorRepository.save(administrator);
  }

  private AdministratorCompany getAdministratorCompanyFromInviteToken(String token) throws AdministratorCompanyNotFoundException, JwtException {
      Pair<Long, Long> idPair = jwtTokenProvider.verifyInviteTokenAndGetCompanyAndAdminId(token);

      return administratorCompanyRepository.findByAdministratorIdAndCompanyId(idPair.getSecond(), idPair.getFirst())
              .orElseThrow(() -> new AdministratorCompanyNotFoundException("AdministratorCompany not found"));
  }

  @Transactional
  public void updateAdministratorCompany(UpdateAdministratorCompanyRequest updateAdministratorCompanyRequest, Long administratorId) {
    AdministratorCompany administratorCompany = getAdministratorCompanyByAdministratorIdAndCompanyId(administratorId, updateAdministratorCompanyRequest.getCompanyId());

    administratorCompany.setEnabled(updateAdministratorCompanyRequest.getEnabled());
    administratorCompany.setRole(AdministratorRole.fromString(updateAdministratorCompanyRequest.getRole()));

    administratorCompanyRepository.save(administratorCompany);
  }

  @Transactional
  public void deleteAdministratorCompany(DeleteAdministratorCompanyRequest deleteAdministratorCompanyRequest) throws AdministratorCompanyNotFoundException {
    List<AdministratorCompany> administratorCompanies = getAdministratorCompaniesByAdministratorIdsAndCompanyId(deleteAdministratorCompanyRequest.getAdministratorIds(), deleteAdministratorCompanyRequest.getCompanyId());

    administratorCompanyRepository.deleteAll(administratorCompanies);
  }

  public boolean existsByUsername(String username) {
    return administratorRepository.existsByUsername(username);
  }

  @Transactional
  public void addAdministratorToCompany(long companyId, Administrator administrator, AdministratorRole role) {
    Company company = companyService.getCompanyById(companyId);

    AdministratorCompany ac = administrator.addCompanyWithRole(
            company,
            role
    );

    ac.setAccepted(true);
    ac.setEnabled(true);

    administrator.setRegistered(new Date());

    administratorRepository.save(administrator);
  }

  public void requestPasswordReset(String email) throws UsernameNotFoundException {
    Administrator administrator = getByUsernameOrEmail(email);

    String token = jwtTokenProvider.generatePasswordResetToken(String.valueOf(administrator.getId()));
    String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
    String resetLink = String.format("%s/reset-password?token=%s", frontendBaseUrl, encodedToken);

    sendGridService.sendPasswordResetEmail(
            email,
            resetLink,
            administrator.getFirstName()
    );
  }

  public void resetPassword(String token, String newPassword) {
    String adminIdString = jwtTokenProvider.verifyPasswordResetTokenAndGetAdminId(token);
    long adminId = Long.parseLong(adminIdString);

    Administrator administrator = getById(adminId);
    passwordPolicyService.validatePassword(newPassword);
    String encodedPassword = passwordEncoder.encode(newPassword);
    administrator.setPassword(encodedPassword);

    administratorRepository.save(administrator);
  }


//  TODO: Add methods for creating, updating, and deleting administrators
}
