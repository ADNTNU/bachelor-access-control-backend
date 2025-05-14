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


  /**
   * Get an Administrator by ID.
   *
   * @param adminId the ID of the Administrator to be retrieved
   * @return the found {@link Administrator} entity
   */
  public Administrator getById(long adminId) {
    return administratorRepository.findById(adminId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**
   * Get an Administrator by username or email.
   * <p>
   * This method retrieves an Administrator entity from the database based on the provided username or email.
   * If the Administrator is not found, it throws a UsernameNotFoundException.
   * </p>
   *
   * @param usernameOrEmail the username or email of the Administrator to be retrieved
   * @return the found {@link Administrator} entity
   * @throws UsernameNotFoundException if the Administrator is not found
   */
  public Administrator getByUsernameOrEmail(String usernameOrEmail) {
    return administratorRepository.findByUsernameOrEmail(usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**
   * Gets a list of AdministratorCompany entities by administrator IDs and company ID.
   * <p>
   * This method retrieves a list of AdministratorCompany entities from the database based on the provided administrator IDs and company ID.
   * If no matching entities are found, an empty list is returned.
   * </p>
   *
   * @param administratorIds the list of administrator IDs to be retrieved
   * @param companyId        the ID of the company to be retrieved
   * @return the list of found {@link AdministratorCompany} entities
   */
  public List<AdministratorCompany> getAdministratorCompaniesByAdministratorIdsAndCompanyId(List<Long> administratorIds, long companyId) {
    return administratorCompanyRepository.findByAdministratorIdsAndCompanyId(administratorIds, companyId);
  }

  /**
   * Gets an AdministratorCompany entity by administrator ID and company ID.
   *
   * @param administratorId the ID of the administrator to be retrieved
   * @param companyId       the ID of the company to be retrieved
   * @return the found {@link AdministratorCompany} entity
   * @throws AdministratorCompanyNotFoundException if the AdministratorCompany is not found
   */
  public AdministratorCompany getAdministratorCompanyByAdministratorIdAndCompanyId(long administratorId, long companyId) throws AdministratorCompanyNotFoundException {
    return administratorCompanyRepository.findByAdministratorIdAndCompanyId(administratorId, companyId)
            .orElseThrow(() -> new AdministratorCompanyNotFoundException("AdministratorCompany not found"));
  }

  /**
   * Gets a response with list of administrator-company relations by company ID.
   * <p>
   * This method retrieves a paginated list of AdministratorCompany entities from the database based on the provided company ID.
   * Then it maps the AdministratorCompany entities to AdministratorListDto entities.
   * It returns a ListResponse containing the mapped entities and pagination details.
   * </p>
   *
   * @param request the request object containing pagination and company ID information
   * @return the {@link ListResponse} containing the list of AdministratorListDto entities and pagination details
   */
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

  /**
   * Tries to invite an administrator to a company by sending an email with an invitation link.
   * <p>
   * This method first checks if the company exists and then creates a new Administrator entity if it doesn't already exist.
   * It generates a temporary password and a unique UUID for the username.
   * Then it adds the company with the specified role to the Administrator entity.
   * Finally, it sends an email with the invite link containing a JWT token.
   * </p>
   *
   * @param inviteAdministratorRequest the request object containing the email, company ID, and role of the administrator
   * @throws CompanyNotFoundException if the company with the specified ID does not exist
   * @throws InvalidRoleException     if the specified role is invalid
   * @throws InvalidKeyException      if the JWT token generation fails
   */
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

  /**
   * Accepts an invitation for an administrator to join a company.
   * <p>
   * This method retrieves the AdministratorCompany entity based on the provided invite token if it is valid.
   * It checks if the administrator is registered and throws an exception if not.
   * Then it sets the accepted status to true and saves the AdministratorCompany entity.
   * </p>
   *
   * @param acceptAdministratorInviteRequest the request object containing the invite token
   * @throws AdministratorCompanyNotFoundException if the AdministratorCompany is not found
   * @throws JwtException                          if the JWT token verification fails
   * @throws IllegalStateException                 if the administrator is not registered
   */
  @Transactional
  public void acceptInvite(AcceptAdministratorInviteRequest acceptAdministratorInviteRequest) throws AdministratorCompanyNotFoundException, JwtException, IllegalStateException {
    AdministratorCompany administratorCompany = getAdministratorCompanyFromInviteToken(acceptAdministratorInviteRequest.getInviteToken());

    Administrator administrator = administratorCompany.getAdministrator();

    if (!administrator.isRegistered()) {
      throw new IllegalStateException("Administrator is not registered");
    }

    administratorCompany.setAccepted(true);

    administratorCompanyRepository.save(administratorCompany);
  }

  /**
   * Registers an administrator and accepts the invitation to join a company.
   * <p>
   * This method retrieves the AdministratorCompany entity based on the provided invite token if it is valid.
   * It updates the administrator's information such as username, password, first name, and last name.
   * It also validates the password against the password policy.
   * Finally, it saves the updated Administrator entity.
   * </p>
   *
   * @param registerAndAcceptAdministratorInviteRequest the request object containing the invite token, username, password, first name, and last name
   * @throws AdministratorCompanyNotFoundException       if the AdministratorCompany is not found
   * @throws JwtException                                if the JWT token verification fails
   * @throws PasswordPolicyService.WeakPasswordException if the password does not meet the policy requirements
   * @throws IllegalArgumentException                    if any of the required fields are missing or invalid
   */
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

  /**
   * Updates the administrator-company relation.
   * <p>
   * This method retrieves the AdministratorCompany entity based on the provided administrator ID and company ID.
   * It updates the details of the AdministratorCompany entity.
   * Finally, it saves the updated AdministratorCompany entity.
   * </p>
   *
   * @param updateAdministratorCompanyRequest the request object containing the company ID, enabled status, and role
   * @param administratorId                   the ID of the administrator to be updated
   * @throws AdministratorCompanyNotFoundException if the AdministratorCompany is not found
   * @throws IllegalStateException                 if no active administrator-company relations would remain after the update
   */
  @Transactional
  public void updateAdministratorCompany(UpdateAdministratorCompanyRequest updateAdministratorCompanyRequest, Long administratorId) {
    AdministratorCompany administratorCompany = getAdministratorCompanyByAdministratorIdAndCompanyId(administratorId, updateAdministratorCompanyRequest.getCompanyId());

    if (administratorCompany.isEnabled() &&
            administratorCompany.isAccepted() &&
            Boolean.FALSE.equals(updateAdministratorCompanyRequest.getEnabled()) &&
            administratorCompanyRepository.countAdministratorCompanyByEnabledIsTrueAndAcceptedIsTrue() <= 1
    ) {
      throw new IllegalStateException("Cannot disable the last enabled administrator-company relation");
    }

    administratorCompany.setEnabled(updateAdministratorCompanyRequest.getEnabled());
    administratorCompany.setRole(AdministratorRole.fromString(updateAdministratorCompanyRequest.getRole()));

    administratorCompanyRepository.save(administratorCompany);
  }

  /**
   * Deletes the given administrator-company relations.
   * <p>
   * This method retrieves a list of AdministratorCompany entities based on the provided administrator IDs and company ID.
   * It deletes all matching AdministratorCompany entities from the database.
   * </p>
   *
   * @param deleteAdministratorCompanyRequest the request object containing the list of administrator IDs and company ID
   * @throws AdministratorCompanyNotFoundException if no matching AdministratorCompany entities are found
   * @throws IllegalStateException                 if no active administrator-company relations would remain after deletion
   */
  @Transactional
  public void deleteAdministratorCompany(DeleteAdministratorCompanyRequest deleteAdministratorCompanyRequest) throws AdministratorCompanyNotFoundException {
    List<AdministratorCompany> administratorCompanies = getAdministratorCompaniesByAdministratorIdsAndCompanyId(deleteAdministratorCompanyRequest.getAdministratorIds(), deleteAdministratorCompanyRequest.getCompanyId());

    long activeToDeleteCount = administratorCompanies.stream()
            .filter(AdministratorCompany::isEnabled)
            .filter(AdministratorCompany::isAccepted)
            .count();

    long enabledCountBeforeDeletion = administratorCompanyRepository.countAdministratorCompanyByEnabledIsTrueAndAcceptedIsTrue() - activeToDeleteCount;

    if (enabledCountBeforeDeletion <= 0) {
      throw new IllegalStateException("No enabled administrator-company relations would remain after deletion");
    }

    administratorCompanyRepository.deleteAll(administratorCompanies);
  }

  /**
   * Directly adds an administrator to a company with the specified role.
   * This method is used when creating a new company or otherwise an invitation is not needed.
   * <p>
   *   This method retrieves the Company entity based on the provided company ID.
   *   It adds the administrator to the company with the specified role.
   * </p>
   *
   * @param companyId the ID of the company to which the administrator is being added
   * @param administrator the administrator to be added
   * @param role the role of the administrator in the company
   * @throws CompanyNotFoundException if the company with the specified ID does not exist
   */
  @Transactional
  public void addAdministratorToCompany(long companyId, Administrator administrator, AdministratorRole role) throws CompanyNotFoundException {
    Company company = companyService.getCompanyById(companyId);

    AdministratorCompany ac = administrator.addCompanyWithRole(
            company,
            role
    );

    ac.setAccepted(true);

    administrator.setRegistered(new Date());

    administratorRepository.save(administrator);
  }

  /**
   * Sends a password reset email to the administrator.
   * <p>
   * This method retrieves the Administrator entity based on the provided email.
   * It generates a password reset token and constructs a reset link.
   * Finally, it sends an email with the reset link to the administrator.
   * </p>
   *
   * @param email the email of the administrator to whom the password reset email is sent
   * @throws UsernameNotFoundException if the administrator is not found
   * @throws InvalidKeyException      if the JWT token generation fails
   * @throws SendMailException        if the email sending fails
   */
  public void requestPasswordReset(String email) throws UsernameNotFoundException, InvalidKeyException, SendMailException {
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

  /**
   * Resets the password for the administrator using the provided token and new password.
   * <p>
   * This method verifies the password reset token and retrieves the administrator ID from it.
   * It validates the new password against the password policy and updates the administrator's password.
   * Finally, it saves the updated Administrator entity.
   * </p>
   *
   * @param token       the password reset token
   * @param newPassword the new password to be set
   * @throws JwtException                          if the JWT token verification fails
   * @throws NumberFormatException                  if the administrator ID in the token is not a valid number
   * @throws UsernameNotFoundException               if the administrator is not found
   * @throws PasswordPolicyService.WeakPasswordException if the new password does not meet the policy requirements
   */
  public void resetPassword(String token, String newPassword) throws JwtException, NumberFormatException, UsernameNotFoundException, PasswordPolicyService.WeakPasswordException {
    String adminIdString = jwtTokenProvider.verifyPasswordResetTokenAndGetAdminId(token);
    long adminId = Long.parseLong(adminIdString);

    Administrator administrator = getById(adminId);
    passwordPolicyService.validatePassword(newPassword);
    String encodedPassword = passwordEncoder.encode(newPassword);
    administrator.setPassword(encodedPassword);

    administratorRepository.save(administrator);
  }
}
