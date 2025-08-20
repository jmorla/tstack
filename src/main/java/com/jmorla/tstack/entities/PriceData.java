package com.jmorla.tstack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("price_data")
public class PriceData {

    @Id
    private Long id;

    @Column("instrument_id")
    private Long instrumentId;

    @Column("timestamp")
    private LocalDateTime timestamp;

    @Column("open_price")
    private BigDecimal openPrice;

    @Column("high_price")
    private BigDecimal highPrice;

    @Column("low_price")
    private BigDecimal lowPrice;

    @Column("close_price")
    private BigDecimal closePrice;

    @Column("volume")
    private BigDecimal volume;
}