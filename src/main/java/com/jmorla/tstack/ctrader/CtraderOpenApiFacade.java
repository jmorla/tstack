package com.jmorla.tstack.ctrader;

import com.google.protobuf.GeneratedMessage;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthRes;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountLogoutReq;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountLogoutRes;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthRes;
import com.xtrader.protocol.openapi.v2.ProtoOASymbolsListReq;
import com.xtrader.protocol.openapi.v2.ProtoOASymbolsListRes;
import com.xtrader.protocol.openapi.v2.ProtoOAGetTrendbarsReq;
import com.xtrader.protocol.openapi.v2.ProtoOAGetTrendbarsRes;
import com.xtrader.protocol.openapi.v2.model.ProtoOATrendbarPeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CtraderOpenApiFacade {

    private static final Logger log = LoggerFactory.getLogger(CtraderOpenApiFacade.class);

    private final CtraderApiClient client;

    public CtraderOpenApiFacade(CtraderApiClient client) {
        this.client = client;
    }

    private <T> CompletableFuture<T> executeRequest(GeneratedMessage request, 
                                                   Class<T> responseType, 
                                                   String startMessage,
                                                   String successMessage,
                                                   String failureMessage,
                                                   Object... logParams) {
        log.info(startMessage, logParams);
        
        return client.request(request)
                .orTimeout(30, TimeUnit.SECONDS)
                .thenApply(responseType::cast)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error(failureMessage, logParams.length > 0 ? logParams[0] : null, throwable);
                    } else {
                        log.info(successMessage, logParams);
                    }
                });
    }

    public CompletableFuture<ProtoOAApplicationAuthRes> authenticateApplication(final String clientId,
                                                                                 final String secret) {
        ProtoOAApplicationAuthReq authReq =
                ProtoOAApplicationAuthReq.newBuilder()
                        .setClientId(clientId)
                        .setClientSecret(secret)
                        .build();

        return executeRequest(authReq, ProtoOAApplicationAuthRes.class, 
                            "Authenticating application",
                            "Application authentication successful",
                            "Application authentication failed");
    }

    public CompletableFuture<ProtoOAAccountAuthRes> authenticateAccount(final long accountId,
                                                                        final String accessToken) {
        ProtoOAAccountAuthReq accountAuthReq =
                ProtoOAAccountAuthReq.newBuilder()
                        .setCtidTraderAccountId(accountId)
                        .setAccessToken(accessToken)
                        .build();

        return executeRequest(accountAuthReq, ProtoOAAccountAuthRes.class, 
                            "Authenticating account with ID: {}",
                            "Account authentication successful for account: {}",
                            "Account authentication failed for account: {}",
                            accountId);
    }

    public CompletableFuture<ProtoOAAccountLogoutRes> logoutAccount(final long accountId) {
        ProtoOAAccountLogoutReq logoutReq =
                ProtoOAAccountLogoutReq.newBuilder()
                        .setCtidTraderAccountId(accountId)
                        .build();

        return executeRequest(logoutReq, ProtoOAAccountLogoutRes.class, 
                            "Logging out account with ID: {}",
                            "Account logout successful for account: {}",
                            "Account logout failed for account: {}",
                            accountId);
    }

    public CompletableFuture<ProtoOASymbolsListRes> getAvailableSymbols(final long accountId, final boolean includeArchived) {
        ProtoOASymbolsListReq symbolsReq =
                ProtoOASymbolsListReq.newBuilder()
                        .setCtidTraderAccountId(accountId)
                        .setIncludeArchivedSymbols(includeArchived)
                        .build();

        return executeRequest(symbolsReq, ProtoOASymbolsListRes.class, 
                            "Fetching available symbols for account: {}",
                            "Successfully fetched symbols for account: {}",
                            "Failed to fetch symbols for account: {}",
                            accountId);
    }

    public CompletableFuture<ProtoOAGetTrendbarsRes> getTrendBarData(final long accountId,
                                                                     final long symbolId,
                                                                     final LocalDateTime fromTime, 
                                                                     final LocalDateTime toTime) {
        ProtoOAGetTrendbarsReq trendBarsReq =
                ProtoOAGetTrendbarsReq.newBuilder()
                        .setCtidTraderAccountId(accountId)
                        .setSymbolId(symbolId)
                        .setPeriod(ProtoOATrendbarPeriod.M1)
                        .setFromTimestamp(fromTime.toEpochSecond(ZoneOffset.UTC) * 1000)
                        .setToTimestamp(toTime.toEpochSecond(ZoneOffset.UTC) * 1000)
                        .build();

        return executeRequest(trendBarsReq, ProtoOAGetTrendbarsRes.class,
                            "Fetching 1-minute trendbar data for symbol {} from {} to {} for account: {}",
                            "Successfully fetched trendbar data for symbol {} from {} to {} for account: {}",
                            "Failed to fetch trendbar data for symbol {} from {} to {} for account: {}",
                            symbolId, fromTime, toTime, accountId);
    }
}
