package com.jmorla.tstack.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PageableRequest {
    private final Integer limit;
    private final Integer offset;

    public PageableRequest(Integer limit, Integer offset) {
        this.limit = Objects.isNull(limit) ? 5 : limit;
        this.offset = Objects.isNull(offset) ? 0 : offset;
    }
}