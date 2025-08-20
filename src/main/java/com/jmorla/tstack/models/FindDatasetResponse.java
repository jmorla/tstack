package com.jmorla.tstack.models;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FindDatasetResponse {
    List<DatasetRecord> datasets;
    int limit;
    int offset;
    long total;
}