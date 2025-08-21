package com.jmorla.tstack.mappers;

import com.jmorla.tstack.dto.ProviderRecord;
import com.jmorla.tstack.entities.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderRecord toRecord(Provider provider) {
        if (provider == null) {
            return null;
        }
        
        return new ProviderRecord(
            provider.getId(),
            provider.getName(),
            provider.getDescription(),
            provider.getCreatedAt(),
            provider.getUpdatedAt()
        );
    }
}