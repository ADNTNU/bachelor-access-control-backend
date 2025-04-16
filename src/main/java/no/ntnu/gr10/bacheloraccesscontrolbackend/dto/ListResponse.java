package no.ntnu.gr10.bacheloraccesscontrolbackend.dto;

import java.util.List;

public record ListResponse<T>(
        List<T> data,
        int totalPages,
        long totalElements
) {}

