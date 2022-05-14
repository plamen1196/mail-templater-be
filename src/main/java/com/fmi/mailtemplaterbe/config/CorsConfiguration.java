package com.fmi.mailtemplaterbe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "web.cors")
public class CorsConfiguration {

    private List<String> allowedOrigins;
}
