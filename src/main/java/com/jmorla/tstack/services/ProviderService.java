package com.jmorla.tstack.services;

import com.jmorla.tstack.dto.ProviderRecord;

import java.util.List;

public interface ProviderService {
    
    List<ProviderRecord> getProviders();
}