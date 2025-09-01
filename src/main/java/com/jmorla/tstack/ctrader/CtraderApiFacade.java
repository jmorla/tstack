package com.jmorla.tstack.ctrader;

import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthRes;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CtraderApiFacade {

    private static final Logger log = LoggerFactory.getLogger(CtraderApiFacade.class);

    private final CtraderApiClient client;

    public CtraderApiFacade(CtraderApiClient client) {
        this.client = client;
    }

    public CompletableFuture<ProtoOAApplicationAuthRes> authenticateApplication(final String clientId,
                                                                                 final String secret) {
        log.info("Authenticating application");

        ProtoOAApplicationAuthReq authReq =
                ProtoOAApplicationAuthReq.newBuilder()
                        .setClientId(clientId)
                        .setClientSecret(secret)
                        .build();

        return client
                .request(authReq)
                .thenApply(e -> (ProtoOAApplicationAuthRes) e)
                .orTimeout(30, TimeUnit.SECONDS)
                .whenComplete(
                        (result, throwable) -> {
                            if (throwable != null) {
                                log.error("Application authentication failed", throwable);
                            } else {
                                log.info("Application authentication successful");
                            }
                        });
    }

    public CompletableFuture<ProtoOAAccountAuthRes> authenticateAccount(final long accountId,
                                                                        final String accessToken) {
        log.info("Authenticating account with ID: {}", accountId);

        ProtoOAAccountAuthReq accountAuthReq =
                ProtoOAAccountAuthReq.newBuilder()
                        .setCtidTraderAccountId(accountId)
                        .setAccessToken(accessToken)
                        .build();

        return client
                .request(accountAuthReq)
                .orTimeout(30, TimeUnit.SECONDS)
                .thenApply(e -> (ProtoOAAccountAuthRes) e)
                .whenComplete(
                        (result, throwable) -> {
                            if (throwable != null) {
                                log.error("Account authentication failed", throwable);
                            } else {
                                log.info("Account authentication successful for account: {}", accountId);
                            }
                        });
    }
}
