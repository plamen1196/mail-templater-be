package com.fmi.mailtemplaterbe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/* TODO: Consider usage with existing templates and FE */
@Configuration
public class EmailTemplatesConfiguration {

    @Value("${email-templates.placeholder-prefix}")
    private String placeholderPrefix;

    @Value("${email-templates.placeholder-suffix}")
    private String placeholderSuffix;

    public String getPlaceholderPrefix() {
        return placeholderPrefix;
    }

    public String getPlaceholderSuffix() {
        return placeholderSuffix;
    }
}
