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
        
        @JsonProperty("client_id")
        private String clientId;
        
        @JsonProperty("client_secret")
        private String clientSecret;
        
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("account_id")
        private long accountId;
        
        private String host;
        private int port;
        
        @JsonProperty("enable_ssl")
        private boolean enableSsl;
        
        @JsonProperty("idle_timeout")
        private int idleTimeout;
        
        @JsonProperty("connection_timeout")
        private int connectionTimeout;
    }
}