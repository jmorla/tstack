package com.jmorla.tstack.services;

import com.jmorla.tstack.mappers.DatasetMapper;
import com.jmorla.tstack.models.FindDatasetRequest;
import com.jmorla.tstack.models.FindDatasetResponse;
import com.jmorla.tstack.repositories.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {

    private final InstrumentRepository instrumentRepository;
    private final DatasetMapper datasetMapper;

    @Override
    public FindDatasetResponse findDatasets(FindDatasetRequest request) {
        var datasets = instrumentRepository.findAllWithProvider(request.getLimit(), request.getOffset())
                .stream().map(datasetMapper::mapToDatasetRecord)
                .toList();

        return FindDatasetResponse.builder()
                .datasets(datasets)
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(instrumentRepository.countAllWithProvider())
                .build();
    }
}
