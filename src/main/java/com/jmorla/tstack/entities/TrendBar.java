package com.jmorla.tstack.entities;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Table("trendbar_history")
public class TrendBar {

    private long id;
    private long symbolId;
    private BigInteger volume;
    private TimeFrame period;
    private BigDecimal lowPrice;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private long utcTimestampInMinutes;
}
