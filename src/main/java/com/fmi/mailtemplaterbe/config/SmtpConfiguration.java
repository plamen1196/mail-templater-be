package com.fmi.mailtemplaterbe.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "email.smtp")
public class SmtpConfiguration {

    private String defaultServerName;
    private List<SmtpServer> servers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SmtpServer {

        private String host;
        private String port;
        private String auth;
        private StartTtls starttls;
        private Ssl ssl;
        private String timeout;
        private String connectiontimeout;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StartTtls {
        private String enable;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ssl {
        private String enable;
    }
}
