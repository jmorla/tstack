package com.jmorla.tstack.services.impl;

import com.jmorla.tstack.dto.ProviderRecord;
import com.jmorla.tstack.mappers.ProviderMapper;
import com.jmorla.tstack.repositories.ProviderRepository;
import com.jmorla.tstack.services.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Implementation of {@link ProviderService} for managing provider-related operations.
 * 
 * <p>This service provides functionality to retrieve and manage data providers
 * in the trading stack system. It acts as a facade over the data access layer,
 * converting between entity objects and data transfer objects.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    /**
     * Retrieves all available providers from the system.
     * 
     * <p>This method fetches all provider entities from the database and converts
     * them to {@link ProviderRecord} objects for consumption by the presentation layer.
     * The results are not cached and represent the current state of the database.</p>
     * 
     * @return a list of all provider records; never {@code null} but may be empty
     * @see ProviderRecord
     * @see ProviderRepository#findAll()
     * @since 1.0
     */
    @Override
    public List<ProviderRecord> getProviders() {
        return StreamSupport.stream(providerRepository.findAll().spliterator(), false)
                .map(providerMapper::toRecord)
                .toList();
    }
}