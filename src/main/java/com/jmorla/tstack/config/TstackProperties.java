package com.jmorla.tstack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tstack")
public class TstackProperties {

    private Providers providers;

    @Data
    public static class Providers {
        private Ctrader ctrader;
    }

    @Data
    public static class Ctrader {
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
