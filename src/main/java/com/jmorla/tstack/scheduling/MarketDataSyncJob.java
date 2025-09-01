package com.jmorla.tstack.scheduling;

import com.jmorla.tstack.config.ApplicationProperties;
import com.jmorla.tstack.ctrader.CtraderApiClient;
import com.jmorla.tstack.services.CTraderMarketDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MarketDataSyncJob {

    private final ApplicationProperties applicationProperties;

    @Scheduled(cron = "0 20 22 * * MON-FRI", zone = "UTC")
    public void onFuturesMarketClose() {
        log.info("Starting market data sync job");
        
        var enabledProviders = applicationProperties.getProviders().getCtrader().stream()
                .filter(ApplicationProperties.CtraderProvider::isEnabled)
                .toList();
        
        if (enabledProviders.isEmpty()) {
            log.warn("No enabled CTrader providers found");
            return;
        }
        
        for (var provider : enabledProviders) {
            try {
                log.info("Processing provider: {}", provider.getName());
                var apiClient = createApiClient(provider);
                var marketDataProvider = new CTraderMarketDataProvider(apiClient, provider);
                
                var instruments = marketDataProvider.getAvailableInstruments();
                log.info("Retrieved {} instruments from CTrader provider: {}", instruments.size(), provider.getName());
            } catch (Exception e) {
                log.error("Error retrieving instruments from CTrader provider: {}", provider.getName(), e);
            }
        }
    }
    
    private CtraderApiClient createApiClient(ApplicationProperties.CtraderProvider provider) {
        return CtraderApiClient.newClient()
                .host(provider.getHost())
                .port(provider.getPort())
                .enableSsl(provider.isEnableSsl())
                .idleTimeMillis(provider.getIdleTimeout())
                .timeoutMillis(provider.getConnectionTimeout())
                .create();
    }

}
