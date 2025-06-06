package no.ntnu.gr10.bacheloraccesscontrolbackend.administrator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.gr10.bacheloraccesscontrolbackend.administratorcompany.AdministratorCompany;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;

/**
 * Represents an administrator entity.
 *
 * <p>This entity is used to store administrator information in the database.
 * The administrator is associated with different companies and has a username
 * and password for authentication.
 * </p>
 *
 * @author Anders Lund
 * @version 06.04.2025
 */
@Getter
@Entity
@Table(name = "administrators")
public class Administrator {
  @Id()
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column
  private Date registered = null;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<AdministratorCompany> administratorCompanies = new HashSet<>();


  /**
   * Default constructor for serialization.
   */
  public Administrator() {
    // Default constructor for JPA
  }

  /**
   * Constructor for creating a new administrator.
   *
   * @param username The username of the administrator.
   * @param password The password of the administrator.
   */
  public Administrator(
          String email,
          String username,
          String password,
          String firstName,
          String lastName
  ) {
    setEmail(email);
    setUsername(username);
    setPassword(password);
    setFirstName(firstName);
    setLastName(lastName);
  }

  /**
   * Get the registered status of the administrator.
   *
   * @return true if the administrator is registered, false otherwise.
   */
  public boolean isRegistered() {
    return registered != null;
  }

  /**
   * Set the email of the administrator.
   *
   * @param email The email to set.
   */
  public void setEmail(String email) throws IllegalArgumentException {
    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }
    if (email.length() > 254) {
      throw new IllegalArgumentException("Email cannot exceed 254 characters");
    }

    if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
      throw new IllegalArgumentException("Email is not valid");
    }

    this.email = email;
  }

  /**
   * Sets the username of the Administrator.
   * Has to not be blank and not exceed 254 in length.
   *
   * @param username The username to set
   * @throws IllegalArgumentException if the username is blank or greater than 254 characters
   */
  public void setUsername(String username) throws IllegalArgumentException {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }
    if (username.length() > 254) {
      throw new IllegalArgumentException("Username cannot exceed 254 characters");
    }

    this.username = username;
  }

  /**
   * Set the password for the administrator.
   *
   * @param password The password to set.
   * @throws IllegalArgumentException if the password is blank
   */
  public void setPassword(String password) throws IllegalArgumentException {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    this.password = password;
  }

  /**
   * Set the first name of the administrator.
   *
   * @param firstName The first name to set.
   * @throws IllegalArgumentException if the firstName is blank or greater than 255 characters
   */
  public void setFirstName(String firstName) throws IllegalArgumentException {
    if (firstName == null || firstName.isEmpty()) {
      throw new IllegalArgumentException("First name cannot be null or empty");
    }
    if (firstName.length() > 255) {
      throw new IllegalArgumentException("First name cannot exceed 255 characters");
    }
    this.firstName = firstName;
  }

  /**
   * Set the last name of the administrator.
   *
   * @param lastName The last name to set.
   * @throws IllegalArgumentException if the lastName is blank or greater than 255 characters
   */
  public void setLastName(String lastName) throws IllegalArgumentException {
    if (lastName == null || lastName.isEmpty()) {
      throw new IllegalArgumentException("Last name cannot be null or empty");
    }
    if (lastName.length() > 255) {
      throw new IllegalArgumentException("Last name cannot exceed 255 characters");
    }
    this.lastName = lastName;
  }

  /**
   * Checks whether two administrators are equal based on their ID and username.
   *
   * @param o The object to compare with.
   * @return true if the administrators are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Administrator other)) {
      return false;
    }

    return Objects.equals(id, other.id) && username.equals(other.username);
  }

  /**
   * Returns the hash code of the administrator based on its ID and username.
   *
   * @return The hash code of the administrator.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  /**
   * Returns a string representation of the administrator.
   *
   * <p>This method returns a string representation of the administrator,
   * including its ID, username, and enabled status.
   * </p>
   *
   * @return A string representation of the administrator.
   */
  @Override
  public String toString() {
    return "Administrator{"
            + "id=" + id
            + ", registered="
            + registered
            + ", username='" + username + '\''
            + '}';
  }

  /**
   * Get the company-administrator links associated with the administrator.
   *
   * @return A collection of {@link AdministratorCompany} links.
   */
  public Collection<AdministratorCompany> getAdministratorCompanies() {
    return administratorCompanies;
  }

  /**
   * Get the companies associated with the administrator.
   *
   * @return A collection of companies associated with the administrator.
   */
  public Collection<Company> getCompanies() {
    Set<Company> companies = new HashSet<>();
    for (AdministratorCompany link : administratorCompanies) {
      companies.add(link.getCompany());
    }
    return companies;
  }

  /**
   * Add a company with a specific role to the administrator.
   *
   * @param company The company to add.
   * @param role    The role of the administrator in the company.
   * @throws IllegalArgumentException if the company is null or if the
   *                                  administrator already has a link to this company.
   */
  public AdministratorCompany addCompanyWithRole(
          Company company,
          AdministratorRole role
  ) throws IllegalArgumentException {
    if (company == null) {
      throw new IllegalArgumentException("Company cannot be null");
    }
    if (administratorCompanies.stream().anyMatch(link -> link.getCompany().equals(company))) {
      throw new IllegalArgumentException("Administrator already has a link to this company");
    }

    AdministratorCompany link = new AdministratorCompany();

    link.setAdministrator(this);
    link.setCompany(company);
    link.setRole(role);

    administratorCompanies.add(link);

    return link;
  }
}
