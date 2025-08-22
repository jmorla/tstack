package com.jmorla.tstack.models;

import lombok.Getter;

@Getter
public class DatasetSearchRequest extends PageableRequest {
    private final String instrument;
    private final String status;
    private final String provider;

    public DatasetSearchRequest(Integer limit, Integer offset, String instrument, String status, String provider) {
        super(limit, offset);
        this.instrument = instrument;
        this.status = status;
        this.provider = provider;
    }
}