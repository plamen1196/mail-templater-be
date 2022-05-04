package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.SmtpConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmtpService {

    private final SmtpConfiguration smtpConfiguration;

    public List<SmtpConfiguration.SmtpServer> getSmtpServers() {
        return smtpConfiguration.getServers();
    }

    public SmtpConfiguration.SmtpServer getDefaultSmtpServer() {
        return smtpConfiguration.getServers().stream()
                .filter(smtpServer -> smtpServer.getName().equalsIgnoreCase(smtpConfiguration.getDefaultServerName()))
                .findFirst()
                .orElse(null);
    }
}
