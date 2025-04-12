package no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto;

/**
 * Temporary DTO for creating a company.
 * A request object for creating a new company.
 * Contains the name of the company and the username of the administrator.
 *
 * @param name                 the name of the company
 * @param administratorUsername the username of the administrator
 */
public record CreateCompanyRequest(String name, String administratorUsername) {
}
