package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey;

import jakarta.persistence.*;
import no.ntnu.gr10.bacheloraccesscontrolbackend.company.Company;
import no.ntnu.gr10.bacheloraccesscontrolbackend.scope.Scope;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an API key entity.
 * <p>
 * This entity is used to store API keys in the database.
 * The API key is used for authentication and authorization purposes.
 * The API key is associated with a specific company, which is the owner of the key.
 * The API key is assigned to different scopes, which define the permissions associated with the key.
 * </p>
 *
 * @author Anders Lund
 * @version 05.05.2025
 */
@Entity
@Table(name = "api_keys")
public class ApiKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private boolean enabled = true;

  @Column(unique = true, nullable = false)
  private String clientId;

  @Column(nullable = false)
  private String clientSecret;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(length = 255)
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "api_key_scopes",
          joinColumns = @JoinColumn(name = "api_key_id"),
          inverseJoinColumns = @JoinColumn(name = "scope_id")
  )
  private final Set<Scope> scopes = new HashSet<>();

  /**
   * Default constructor for JPA.
   * <p>
   * This constructor is used by JPA to create instances of the entity.
   * </p>
   */
  public ApiKey() {
    // Default constructor for JPA
  }

  /**
   * Constructor for creating a new API key.
   *
   * @param company     The company associated with the API key.
   * @param name        The name of the API key.
   * @param description A description of the API key.
   *
   */
  public ApiKey(boolean enabled, Company company, String name, String description, String clientId, String clientSecret) {
    setEnabled(enabled);
    setCompany(company);
    setName(name);
    setDescription(description);
    setClientId(clientId);
    setClientSecret(clientSecret);
  }

  /**
   * Gets the ID of the API key.
   * @return The ID of the API key.
   */
  public Long getId() {
    return id;
  }

  /**
   * Gets the enabled status of the API key.
   * @return The enabled status of the API key.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the enabled status of the API key.
   * @param enabled The enabled status to set.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Gets the client ID of the API key.
   * @return The client ID of the API key.
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets the client ID of the API key.
   * @param clientId The client ID to set.
   * @throws IllegalArgumentException if the client ID is null or empty.
   */
  public void setClientId(String clientId) {
    if (clientId == null || clientId.isEmpty()) {
      throw new IllegalArgumentException("Client ID cannot be null or empty");
    }

    if (clientId.length() > 255) {
      throw new IllegalArgumentException("Client ID cannot exceed 255 characters");
    }

    this.clientId = clientId;
  }

  /**
   * Gets the client secret of the API key.
   * @return The client secret of the API key.
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * Sets the client secret of the API key.
   * @param clientSecret The client secret to set.
   * @throws IllegalArgumentException if the client secret is null or empty.
   */
  public void setClientSecret(String clientSecret) {
    if (clientSecret == null || clientSecret.isEmpty()) {
      throw new IllegalArgumentException("Client secret cannot be null or empty");
    }

    if (clientSecret.length() > 255) {
      throw new IllegalArgumentException("Client secret cannot exceed 255 characters");
    }

    this.clientSecret = clientSecret;
  }

  /**
   * Gets the name of the API key.
   * @return The name of the API key.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the API key.
   * @param name The name to set.
   * @throws IllegalArgumentException if the name is null or empty.
   */
  public void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }

    if (name.length() > 255) {
      throw new IllegalArgumentException("Name cannot exceed 255 characters");
    }

    this.name = name;
  }

  /**
   * Gets the description of the API key.
   * @return The description of the API key.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the API key.
   * @param description The description to set.
   * @throws IllegalArgumentException if the description exceeds 255 characters.
   */
  public void setDescription(String description) {
    if (description != null && description.length() > 255) {
      throw new IllegalArgumentException("Description cannot exceed 255 characters");
    }

    this.description = description;
  }

  /**
   * Gets the company associated with the API key.
   * @return The company associated with the API key.
   */
  public Company getCompany() {
    return company;
  }

  /**
   * Sets the company associated with the API key.
   * @param company The company to set.
   * @throws IllegalArgumentException if the company is null.
   */
  public void setCompany(Company company) {
    if (company == null) {
      throw new IllegalArgumentException("Company cannot be null");
    }
    this.company = company;
  }

  /**
   * Gets the scopes associated with the API key.
   * @return The scopes associated with the API key.
   */
  public Set<Scope> getScopes() {
    return scopes;
  }

  /**
   * Sets the scopes associated with the API key.
   * @param scopes The scopes to set.
   * @throws IllegalArgumentException if the scopes are null.
   */
  public void setScopes(Collection<Scope> scopes) {
    if (scopes == null) {
      throw new IllegalArgumentException("Scopes cannot be null");
    }
    this.scopes.clear();
    this.scopes.addAll(scopes);
  }


  /**
   * Checks if two API keys are equal.
   * <p>
   *   This method checks if two API keys are equal based on their ID and client ID.
   *   </p>
   * @param o The object to compare with.
   * @return True if the API keys are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ApiKey apiKey)) return false;
    return Objects.equals(id, apiKey.id) && Objects.equals(clientId, apiKey.clientId);
  }

  /**
   * Returns the hash code of the API key.
   * <p>
   *   This method returns the hash code of the API key based on its ID and client ID.
   *   </p>
   * @return The hash code of the API key.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, clientId);
  }

  /**
   * Returns a string representation of the API key.
   * <p>
   *   This method returns a string representation of the API key, including its ID, client ID, name, description, and company.
   *   Excludes the client secret for security reasons.
   *   </p>
   * @return A string representation of the API key.
   */
  @Override
  public String toString() {
    return "ApiKey{" +
            "id=" + id +
            ", clientId='" + clientId + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", company=" + company +
            '}';
  }
}
