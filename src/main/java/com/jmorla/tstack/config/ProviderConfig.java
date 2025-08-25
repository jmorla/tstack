package com.jmorla.tstack.config;

import com.jmorla.tstack.ctrader.CtraderApiClient;
import com.jmorla.tstack.ctrader.CtraderAuthenticator;
import com.jmorla.tstack.services.MarketDataProvider;
import com.jmorla.tstack.services.NettyMarketDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TstackProperties.class)
public class ProviderConfig {

    @Bean(name = "pepperstoneApiClient")
    public CtraderApiClient ctraderApiClient(TstackProperties properties) {
        TstackProperties.Ctrader ctraderProperties = properties.getProviders().getCtrader();
        var client = CtraderApiClient.newClient()
                .host(ctraderProperties.getHost())
                .port(ctraderProperties.getPort())
                .enableSsl(ctraderProperties.isEnableSsl())
                .idleTimeMillis(ctraderProperties.getIdleTimeout())
                .timeoutMillis(ctraderProperties.getConnectionTimeout())
                .create();
        var authenticator = new CtraderAuthenticator(client, properties);
        authenticator.authenticate();

        return client;
    }

    @Bean(name = "pepperstoneDataProvider")
    public MarketDataProvider pepperstoneDataProvider(
            @Qualifier("pepperstoneApiClient") CtraderApiClient client,
            TstackProperties properties) {
        return new NettyMarketDataProvider(client, properties);
    }
}
