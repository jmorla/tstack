package com.jmorla.tstack.models;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
public class PagedResponse<E> {
    Collection<E> content;
    int limit;
    int offset;
    long total;
}
