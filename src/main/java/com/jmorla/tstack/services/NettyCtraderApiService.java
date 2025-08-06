package com.jmorla.tstack.services;

import com.jmorla.tstack.config.TstackProperties;
import com.jmorla.tstack.ctrader.CtraderApiClient;
import com.jmorla.tstack.exception.TstackException;
import com.jmorla.tstack.mappers.CtraderMapper;
import com.jmorla.tstack.models.SymbolRecord;
import com.xtrader.protocol.openapi.v2.ProtoOASymbolsListReq;
import com.xtrader.protocol.openapi.v2.ProtoOASymbolsListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class NettyCtraderApiService implements CtraderApiService {

    private final CtraderApiClient apiClient;
    private final TstackProperties properties;

    @Override
    public List<SymbolRecord> getAllAvailableSymbols() {
        var req = ProtoOASymbolsListReq.newBuilder()
                .setIncludeArchivedSymbols(false)
                .setCtidTraderAccountId(properties.getProviders().getCtrader().getAccountId())
                .build();

        var future = apiClient.request(req)
                .thenApply(res -> ((ProtoOASymbolsListRes) res).getSymbolList().stream()
                        .map(CtraderMapper::mapToSymbolRecord).toList());

        return get(future);
    }

    public <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TstackException(e);
        }
    }
}
