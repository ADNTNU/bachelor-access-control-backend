package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

public record CreateApiKeyResponse(
        long id,
        String clientId,
        String clientSecret
){}
