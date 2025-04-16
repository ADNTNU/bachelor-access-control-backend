package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;


import jakarta.persistence.EntityNotFoundException;

@SuppressWarnings("squid:S110")
public class ApiKeyNotFoundException extends EntityNotFoundException {
    public ApiKeyNotFoundException(String message) {
      super(message);
    }
}
