package com.jmorla.tstack.services;

import com.jmorla.tstack.models.DatasetRecord;
import com.jmorla.tstack.models.PageableRequest;
import com.jmorla.tstack.models.PagedResponse;
import org.springframework.stereotype.Service;

@Service
public interface DatasetService {

    PagedResponse<DatasetRecord> findDatasets(PageableRequest request);
}