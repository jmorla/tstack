package com.jmorla.tstack.config;

import com.jmorla.tstack.ctrader.CtraderApiClient;
import com.jmorla.tstack.ctrader.CtraderAuthenticator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TstackProperties.class)
public class CtraderConfig {

    @Bean
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
}
