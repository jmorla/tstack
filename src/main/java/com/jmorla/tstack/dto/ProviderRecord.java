package com.jmorla.tstack.dto;

import java.time.LocalDateTime;

public record ProviderRecord(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}