package com.jmorla.tstack.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Table("symbols")
public class Symbol {

    @Id
    private long id;
    private long platformId;
    private String name;
    private String description;
    private int digits;
    private boolean enabled;
    private boolean shortSellingEnabled;
    private BigDecimal swapLong;
    private BigDecimal swapShort;
    private BigInteger maxVolume;
    private BigInteger minVolume;
    private BigInteger stepVolume;
    private BigInteger maxExposure;
    private String scheduleTimeZone;
    private String measurementUnits;
    private BigInteger minCommission;

}
