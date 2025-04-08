package no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests;

/**
 * Data Transfer Object (DTO) for registration requests.
 * @param username The username of the user attempting to register.
 * @param password The password of the user attempting to register.
 * @param firstName The first name of the user attempting to register.
 * @param lastName The last name of the user attempting to register.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
public record RegisterRequest(String username, String password, String firstName, String lastName) {
}
