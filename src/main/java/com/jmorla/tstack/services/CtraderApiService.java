package com.jmorla.tstack.services;

import com.jmorla.tstack.models.SymbolRecord;

import java.util.List;

public interface CtraderApiService {

    List<SymbolRecord> getAllAvailableSymbols();

}
