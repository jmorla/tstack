package com.jmorla.tstack.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Providers providers;

    @Data
    public static class Providers {
        private List<CtraderProvider> ctrader;
    }

    @Data
    public static class CtraderProvider {
        private String name;
        private boolean enabled;

        private String clientId;
        private String clientSecret;
        private String accessToken;
        private long accountId;
        private String host;
        private int port;
        private boolean enableSsl;
        private int idleTimeout;
        private int connectionTimeout;
    }
}