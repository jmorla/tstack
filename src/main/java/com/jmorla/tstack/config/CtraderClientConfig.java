package com.jmorla.tstack.config;

import com.jmorla.tstack.ctrader.CtraderApiClient;
import com.jmorla.tstack.ctrader.CtraderOpenApiFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for CTrader API client beans.
 * 
 * <p>This configuration creates Spring beans for CtraderApiClient instances
 * based on the application properties configuration. Each enabled CTrader provider
 * gets its own configured client instance.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CtraderClientConfig {

    private final ApplicationProperties applicationProperties;

    /**
     * Creates a CtraderApiClient bean for the primary CTrader provider.
     * 
     * <p>This method creates a CtraderApiClient instance using the first enabled
     * CTrader provider configuration from the application properties. The client
     * is configured with connection parameters including host, port, SSL settings,
     * and timeout values.</p>
     * 
     * @return a configured CtraderApiClient instance
     * @throws IllegalStateException if no enabled CTrader providers are configured
     * @since 1.0
     */
    @Bean
    @ConditionalOnProperty(name = "app.providers.ctrader[0].enabled", havingValue = "true")
    public CtraderApiClient ctraderApiClient() {
        log.info("Creating CtraderApiClient bean");
        
        var ctraderProviders = applicationProperties.getProviders().getCtrader();
        if (ctraderProviders == null || ctraderProviders.isEmpty()) {
            throw new IllegalStateException("No CTrader providers configured");
        }
        
        // Get the first enabled provider
        var provider = ctraderProviders.stream()
                .filter(ApplicationProperties.CtraderProvider::isEnabled)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No enabled CTrader providers found"));
        
        log.debug("Configuring CtraderApiClient with host: {}, port: {}, SSL: {}", 
                provider.getHost(), provider.getPort(), provider.isEnableSsl());
        
        return CtraderApiClient.newClient()
                .host(provider.getHost())
                .port(provider.getPort())
                .enableSsl(provider.isEnableSsl())
                .idleTimeMillis(provider.getIdleTimeout())
                .timeoutMillis(provider.getConnectionTimeout())
                .create();
    }

    /**
     * Creates a CtraderOpenApiFacade bean with authenticated application and account.
     * 
     * <p>This method creates a CtraderOpenApiFacade instance using the CtraderApiClient
     * and automatically performs application and account authentication using the
     * credentials from the application properties. The facade is ready to use for
     * API operations after successful authentication.</p>
     * 
     * @param ctraderApiClient the CtraderApiClient instance to use
     * @return a configured and authenticated CtraderOpenApiFacade instance
     * @throws RuntimeException if authentication fails
     * @since 1.0
     */
    @Bean
    @ConditionalOnProperty(name = "app.providers.ctrader[0].enabled", havingValue = "true")
    public CtraderOpenApiFacade ctraderOpenApiFacade(CtraderApiClient ctraderApiClient) {
        log.info("Creating CtraderOpenApiFacade bean with authentication");
        
        var ctraderProviders = applicationProperties.getProviders().getCtrader();
        var provider = ctraderProviders.stream()
                .filter(ApplicationProperties.CtraderProvider::isEnabled)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No enabled CTrader providers found"));
        
        var facade = new CtraderOpenApiFacade(ctraderApiClient);
        
        try {
            // Authenticate application
            log.info("Authenticating CTrader application for provider: {}", provider.getName());
            var appAuth = facade.authenticateApplication(provider.getClientId(), provider.getClientSecret())
                    .get();
            log.info("Application authentication successful");
            
            // Authenticate account
            log.info("Authenticating CTrader account: {}", provider.getAccountId());
            var accountAuth = facade.authenticateAccount(provider.getAccountId(), provider.getAccessToken())
                    .get();
            log.info("Account authentication successful");
            
            return facade;
            
        } catch (Exception e) {
            log.error("Failed to authenticate CTrader application/account for provider: {}", 
                     provider.getName(), e);
            throw new RuntimeException("CTrader authentication failed", e);
        }
    }
}