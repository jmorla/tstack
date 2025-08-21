package com.jmorla.tstack.services.impl;

import com.jmorla.tstack.mappers.DatasetMapper;
import com.jmorla.tstack.models.DatasetRecord;
import com.jmorla.tstack.models.FindDatasetRequest;
import com.jmorla.tstack.models.FindDatasetResponse;
import com.jmorla.tstack.models.PagedResponse;
import com.jmorla.tstack.repositories.InstrumentRepository;
import com.jmorla.tstack.services.DatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the DatasetService interface for managing dataset operations.
 * 
 * <p>This service provides functionality to retrieve datasets with their associated
 * providers, including pagination support. It acts as a bridge between the web layer
 * and the data access layer, handling the transformation of domain entities into
 * appropriate response objects.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 * @see DatasetService
 * @see InstrumentRepository
 * @see DatasetMapper
 */
@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {

    private final InstrumentRepository instrumentRepository;
    private final DatasetMapper datasetMapper;

    /**
     * Retrieves a paginated list of datasets with their associated providers.
     * 
     * <p>This method fetches instruments from the repository that have associated
     * providers.</p>
     * 
     * @param request the find dataset request containing pagination parameters
     *                (limit and offset) for controlling the result set size and starting point
     * @return a FindDatasetResponse containing the list of datasets, pagination metadata,
     *         and total count of available datasets with providers
     * @throws IllegalArgumentException if the request parameter is null
     * @since 1.0
     * @see FindDatasetRequest
     * @see FindDatasetResponse
     */
    @Override
    public PagedResponse<DatasetRecord> findDatasets(FindDatasetRequest request) {
        var datasets = instrumentRepository.findAllWithProvider(request.getLimit(), request.getOffset())
                .stream().map(datasetMapper::mapToDatasetRecord)
                .toList();

        return PagedResponse
                .<DatasetRecord>builder()
                .content(datasets)
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(instrumentRepository.countAllWithProvider())
                .build();
    }
}
