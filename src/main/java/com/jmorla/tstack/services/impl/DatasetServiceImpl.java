package com.jmorla.tstack.services.impl;

import com.jmorla.tstack.mappers.DatasetMapper;
import com.jmorla.tstack.models.*;
import com.jmorla.tstack.repositories.InstrumentRepository;
import com.jmorla.tstack.services.DatasetService;
import com.jmorla.tstack.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
@Slf4j
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
     * @see PageableRequest
     * @see FindDatasetResponse
     */
    @Override
    public PagedResponse<DatasetRecord> findDatasets(PageableRequest request) {
        log.debug("Finding datasets with pagination - limit: {}, offset: {}", request.getLimit(), request.getOffset());
        
        var datasets = instrumentRepository.findAllWithProvider(request.getLimit(), request.getOffset())
                .stream().map(datasetMapper::mapToDatasetRecord)
                .toList();

        var totalCount = instrumentRepository.countAllWithProvider();
        log.info("Found {} datasets out of {} total", datasets.size(), totalCount);

        return PagedResponse
                .<DatasetRecord>builder()
                .content(datasets)
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(totalCount)
                .build();
    }

    /**
     * Searches for datasets based on the provided filter criteria with pagination support.
     * 
     * <p>This method performs a filtered search across instruments and their associated
     * providers and dataset metadata.</p>
     * 
     * @param request the search request containing filter criteria and pagination parameters.
     * @return a PagedResponse containing the filtered list of datasets, pagination metadata.
     * @throws IllegalArgumentException if the request parameter is null
     * @since 1.0
     * @see DatasetSearchRequest
     * @see PagedResponse
     * @see DatasetRecord
     */
    @Override
    public PagedResponse<DatasetRecord> searchDatasets(DatasetSearchRequest request) {
        log.debug("Searching datasets with filters - instrument: {}, status: {}, provider: {}, limit: {}, offset: {}", 
                request.getInstrument(), request.getStatus(), request.getProvider(), request.getLimit(), request.getOffset());
        
        Objects.requireNonNull(request, "request object cannot be null");
        
        String instrument = StringUtils.hasValue(request.getInstrument()) 
            ? StringUtils.concat("%", request.getInstrument(), "%") 
            : null;
        String status = StringUtils.hasValue(request.getStatus()) ? request.getStatus() : null;
        String provider = StringUtils.hasValue(request.getProvider()) 
            ? StringUtils.concat("%", request.getProvider(), "%") 
            : null;
        
        log.debug("Processed search filters - instrument: {}, status: {}, provider: {}", instrument, status, provider);
        
        var datasets = instrumentRepository.searchInstrumentsWithProvider(
                instrument,
                status,
                provider,
                request.getLimit(),
                request.getOffset()
        ).stream().map(datasetMapper::mapToDatasetRecord).toList();

        var totalCount = instrumentRepository.countSearchInstrumentsWithProvider(
                instrument,
                status,
                provider
        );
        
        log.info("Search completed - found {} datasets out of {} total matching criteria", datasets.size(), totalCount);

        return PagedResponse
                .<DatasetRecord>builder()
                .content(datasets)
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(totalCount)
                .build();
    }

    @Override
    public void downloadDataset(DatasetDownloadRequest request) {

    }
}
