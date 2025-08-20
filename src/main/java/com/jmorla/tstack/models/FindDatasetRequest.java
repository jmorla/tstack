package com.jmorla.tstack.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindDatasetRequest {
    int limit;
    int offset;
}