package no.ntnu.gr10.bacheloraccesscontrolbackend.exception;

import jakarta.persistence.EntityNotFoundException;

@SuppressWarnings("squid:S110")
public class ScopeNotFoundException extends EntityNotFoundException {
    public ScopeNotFoundException(String message) {
        super(message);
    }
}
