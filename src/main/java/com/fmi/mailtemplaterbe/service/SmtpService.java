package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.SmtpConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
                        return new PasswordAuthentication(getUsername(), getPassword());
                    }
                });
        return session;
    }

    public Session createSMTPSession(String username, String password, String smtpServerName) {
        final SmtpConfiguration.SmtpServer smtpServer = getSmtpServerByName(smtpServerName);
        final Properties prop = new Properties();

        prop.put("mail.smtp.host", smtpServer.getHost());
        prop.put("mail.smtp.port", smtpServer.getPort());
        prop.put("mail.smtp.auth", smtpServer.getAuth());
        prop.put("mail.smtp.starttls.enable", smtpServer.getStarttls().getEnable()); // TLS
        prop.put("mail.smtp.ssl.trust", smtpServer.getHost());
        prop.put("mail.smtp.timeout", smtpServer.getTimeout());
        prop.put("mail.smtp.connectiontimeout", smtpServer.getConnectiontimeout());

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SmtpConfiguration.SmtpServer getDefaultSmtpServer() {
        return smtpConfiguration.getServers().stream()
                .filter(smtpServer -> smtpServer.getName().equalsIgnoreCase(smtpConfiguration.getDefaultServerName()))
                .findFirst()
                .orElse(null);
    }

    public List<SmtpConfiguration.SmtpServer> getAllSmtpServer() {
        return smtpConfiguration.getServers();
    }

    public boolean smtpServerByNameExists(String smtpServerName) {
        try {
            getSmtpServerByName(smtpServerName);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private SmtpConfiguration.SmtpServer getSmtpServerByName(String smtpServerName) {
        if (StringUtils.isEmpty(smtpServerName)) {
            throw new IllegalArgumentException("Missing smtpServerName.");
        }

        return smtpConfiguration.getServers().stream()
                .filter(smtpServer -> smtpServer.getName().equalsIgnoreCase(smtpServerName))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Smtp server with name " + smtpServerName + " was not found."));
    }
}
