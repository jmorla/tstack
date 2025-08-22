package com.jmorla.tstack.services;

import com.jmorla.tstack.models.ProviderRecord;

import java.util.List;

public interface ProviderService {
    
    List<ProviderRecord> getProviders();
}