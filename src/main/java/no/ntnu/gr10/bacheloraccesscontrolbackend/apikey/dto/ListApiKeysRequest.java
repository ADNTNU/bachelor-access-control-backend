package no.ntnu.gr10.bacheloraccesscontrolbackend.apikey.dto;

public record ListApiKeysRequest(int page, int size, long companyId) {
}
