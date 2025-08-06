package com.jmorla.tstack.ctrader;

import com.jmorla.tstack.config.TstackProperties;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAAccountAuthRes;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthReq;
import com.xtrader.protocol.openapi.v2.ProtoOAApplicationAuthRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CtraderAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(CtraderAuthenticator.class);

    private final TstackProperties.Ctrader config;
    private final CtraderApiClient client;

    public CtraderAuthenticator(CtraderApiClient client, TstackProperties config) {
        this.client = client;
        this.config = config.getProviders().getCtrader();
    }

    public void authenticate() {
        log.info("Starting cTrader authentication process...");

        try {
            authenticateApplication()
                    .thenCompose(this::authenticateAccount)
                    .thenRun(() -> log.info("cTrader authentication completed successfully"))
                    .get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<ProtoOAApplicationAuthRes> authenticateApplication() {
        log.info("Authenticating application with client ID: {}", maskClientId(config.getClientId()));

        ProtoOAApplicationAuthReq authReq =
                ProtoOAApplicationAuthReq.newBuilder()
                        .setClientId(config.getClientId())
                        .setClientSecret(config.getClientSecret())
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

    private CompletableFuture<ProtoOAAccountAuthRes> authenticateAccount(
            ProtoOAApplicationAuthRes appAuthRes) {
        log.info("Authenticating account with ID: {}", config.getAccountId());

        ProtoOAAccountAuthReq accountAuthReq =
                ProtoOAAccountAuthReq.newBuilder()
                        .setCtidTraderAccountId(config.getAccountId())
                        .setAccessToken(config.getAccessToken())
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
                                log.info(
                                        "Account authentication successful for account: {}", config.getAccountId());
                            }
                        });
    }

    private String maskClientId(String clientId) {
        if (clientId == null || clientId.length() <= 8) {
            return "****";
        }
        return "%s****%s"
                .formatted(clientId.substring(0, 4), clientId.substring(clientId.length() - 4));
    }
}
