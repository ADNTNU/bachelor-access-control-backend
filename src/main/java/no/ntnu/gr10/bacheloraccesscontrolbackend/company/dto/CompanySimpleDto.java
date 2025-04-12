package no.ntnu.gr10.bacheloraccesscontrolbackend.company.dto;

/**
 * A simple DTO for a company.
 * Used for displaying a list of companies.
 *
 * @param id   the ID of the company
 * @param name the name of the company
 */
public record CompanySimpleDto(long id, String name) {}
