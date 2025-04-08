package no.ntnu.gr10.bacheloraccesscontrolbackend.dto.responses;

/**
 * Response object for authentication requests.
 */
public record AuthenticationResponse(String token) {

  private static final String tokenType = "Bearer";

  public String getTokenType() {
    return tokenType;
  }
}
