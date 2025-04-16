package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import java.util.List;

public record UpdateApiKeyRequest(
        boolean enabled,
        String name,
        String description,
        long companyId,
        List<String> scopes
){
}
