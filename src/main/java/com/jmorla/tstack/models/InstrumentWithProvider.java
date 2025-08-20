package com.jmorla.tstack.models;

import java.time.LocalDateTime;

public record InstrumentWithProvider(
        Long id,
        Long providerId,
        String platformId,
        String symbol,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String providerName,
        String providerDescription,
        LocalDateTime providerCreatedAt,
        LocalDateTime providerUpdatedAt,
        Long datasetId,
        Long totalRecords,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long datasetSizeBytes,
        String status,
        LocalDateTime datasetCreatedAt,
        LocalDateTime datasetUpdatedAt
) {}
