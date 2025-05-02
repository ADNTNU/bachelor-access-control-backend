package no.ntnu.gr10.bacheloraccesscontrolbackend.auth.dto;

/**
 * Data Transfer Object (DTO) for login requests.
 *
 * @author Anders Lund
 * @version 07.04.2025
 */
public class LoginRequest {
        private String usernameOrEmail;
        private String password;

        public LoginRequest() {
            // Default constructor for deserialization
        }

        public LoginRequest(String usernameOrEmail, String password) {
            this.usernameOrEmail = usernameOrEmail;
            this.password = password;
        }

        public String getUsernameOrEmail() {
            return usernameOrEmail;
        }

        public void setUsernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
}
