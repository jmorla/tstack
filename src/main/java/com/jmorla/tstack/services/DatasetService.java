package com.jmorla.tstack.services;

import com.jmorla.tstack.models.FindDatasetRequest;
import com.jmorla.tstack.models.FindDatasetResponse;
import org.springframework.stereotype.Service;

@Service
public interface DatasetService {

    FindDatasetResponse findDatasets(FindDatasetRequest request);
}