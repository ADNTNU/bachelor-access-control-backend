package no.ntnu.gr10.bacheloraccesscontrolbackend.dto.requests;

/**
 * Data Transfer Object (DTO) for login requests.
 * @param username The username of the user attempting to log in.
 * @param password The password of the user attempting to log in.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
public record LoginRequest(String username, String password) {
}
