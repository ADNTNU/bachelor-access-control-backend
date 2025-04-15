package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

import java.util.List;

public record CreateApiKeyRequest(
        boolean enabled,
        String name,
        String description,
        long companyId,
        List<String> scopes
){
}
