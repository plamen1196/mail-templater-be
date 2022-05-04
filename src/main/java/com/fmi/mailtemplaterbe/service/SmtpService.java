package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.SmtpConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class SmtpService {

    private final SmtpConfiguration smtpConfiguration;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    public Session createSMTPSession() {
        final SmtpConfiguration.SmtpServer defaultSmtpServer = getDefaultSmtpServer();
        final Properties prop = new Properties();

        prop.put("mail.smtp.host", defaultSmtpServer.getHost());
        prop.put("mail.smtp.port", defaultSmtpServer.getPort());
        prop.put("mail.smtp.auth", defaultSmtpServer.getAuth());
        prop.put("mail.smtp.starttls.enable", defaultSmtpServer.getStarttls().getEnable()); // TLS
        prop.put("mail.smtp.ssl.trust", defaultSmtpServer.getHost());
        prop.put("mail.smtp.timeout", defaultSmtpServer.getTimeout());
        prop.put("mail.smtp.connectiontimeout", defaultSmtpServer.getConnectiontimeout());

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

    private SmtpConfiguration.SmtpServer getDefaultSmtpServer() {
        return smtpConfiguration.getServers().stream()
                .filter(smtpServer -> smtpServer.getName().equalsIgnoreCase(smtpConfiguration.getDefaultServerName()))
                .findFirst()
                .orElse(null);
    }
}
