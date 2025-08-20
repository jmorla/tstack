package com.jmorla.tstack.mappers;

import com.jmorla.tstack.models.DatasetRecord;
import com.jmorla.tstack.models.InstrumentWithProvider;
import org.springframework.stereotype.Component;

@Component
public class DatasetMapper {

    public DatasetRecord mapToDatasetRecord(InstrumentWithProvider instrumentWithProvider) {
        return DatasetRecord.builder()
                .symbol(instrumentWithProvider.symbol())
                .name(instrumentWithProvider.name())
                .startDate(instrumentWithProvider.startDate())
                .endDate(instrumentWithProvider.endDate())
                .provider(instrumentWithProvider.providerName())
                .totalRecords(instrumentWithProvider.totalRecords())
                .status(instrumentWithProvider.status())
                .build();
    }
}