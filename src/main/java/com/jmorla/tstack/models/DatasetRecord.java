package com.jmorla.tstack.models;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DatasetRecord {
    String symbol;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String provider;
    Long totalRecords;
    String status;
}