package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.EmailTemplatesConfiguration;
import com.fmi.mailtemplaterbe.config.SmtpConfiguration;
import com.fmi.mailtemplaterbe.domain.enums.EmailErrorCategory;
import com.fmi.mailtemplaterbe.domain.resource.*;
import com.fmi.mailtemplaterbe.util.ConfirmationTokenUtil;
import com.fmi.mailtemplaterbe.util.EmailMessageUtil;
import com.fmi.mailtemplaterbe.util.SentEmailsLocalDateTimeComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailManagerService {

    private final EmailTemplatesConfiguration emailTemplatesConfiguration;
    private final EmailHistoryService emailHistoryService;
    private final SmtpService smtpService;

    /**
     * Get the default smtp server that is being used for sending emails.
     *
     * @return default smtp server
     */
    public SmtpServerResource getDefaultSmtpServer() {
        SmtpConfiguration.SmtpServer smtpServer = smtpService.getDefaultSmtpServer();

        return SmtpServerResource.builder()
                .host(smtpServer.getHost())
                .name(smtpServer.getName())
                .build();
    }

    /**
     * Get all smtp servers from the configuration.
     *
     * @return smtp servers
     */
    public List<SmtpServerResource> getAllSmtpServers() {
        return smtpService.getAllSmtpServer().stream().map(
                smtpServer ->
                        SmtpServerResource.builder()
                                .host(smtpServer.getHost())
                                .name(smtpServer.getName())
                                .build())
                .collect(Collectors.toList());
    }

    /**
     * Sends an email to multiple recipients based on the same email template and a different implementation
     * of the placeholders for that email template for each recipient.
     *
     * @param sendEmailResource SendEmailResource
     * @return number of successfully sent emails
     */
    public int sendEmails(SendEmailResource sendEmailResource) {
        return sendEmailToRecipients(sendEmailResource);
    }

    /**
     * Returns a list of preview emails based on the same email template and a different implementation
     * of the placeholders for that email template for each recipient.
     *
     * @param previewEmailResource PreviewEmailResource
     * @return list of preview emails
     */
    public List<RecipientEmailPreview> getPreviewEmails(PreviewEmailResource previewEmailResource) {
        return buildPreviewEmails(previewEmailResource);
    }

    /**
     * Get a list with information about the sent emails, filtered by the respective parameters.
     *
     * @param subject          Subject
     * @param senderEmail      Sender email
     * @param recipientEmail   Recipient email
     * @param sentSuccessfully Sent successfully or not
     * @param confirmation     Confirmation value
     * @param startDate        Start date
     * @param endDate          End date
     * @return list with information about the sent emails
     */
    public List<SentEmailResource> getSentEmails(
            String subject,
            String senderEmail,
            String recipientEmail,
            Boolean sentSuccessfully,
            Long confirmation,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        final List<SentEmailResource> sentEmails = emailHistoryService.getSentEmails(
                subject, senderEmail, recipientEmail, sentSuccessfully, confirmation, startDate, endDate);

        Collections.sort(sentEmails, new SentEmailsLocalDateTimeComparator());

        return sentEmails;
    }

    private int sendEmailToRecipients(SendEmailResource sendEmailResource) {
        int errorsCount = 0;

        for (Recipient recipient : sendEmailResource.getRecipients()) {
            try {
                final String emailSubject = sendEmailResource.getTitle();
                final String emailMessage =
                        EmailMessageUtil.buildEmailMessage(
                                sendEmailResource.getMessage(),
                                recipient.getPlaceholders(),
                                emailTemplatesConfiguration.getPlaceholderPrefix(),
                                emailTemplatesConfiguration.getPlaceholderSuffix());
                final boolean isHtmlMessage = sendEmailResource.getIsHtml();

                sendEmailToRecipient(sendEmailResource.getCredentials(), recipient.getEmail(), emailSubject, emailMessage, isHtmlMessage);
            } catch (Exception e) {
                errorsCount++;
            }
        }

        return sendEmailResource.getRecipients().size() - errorsCount;
    }

    private void sendEmailToRecipient(CredentialsResource credentials, String to, String subject, String content, boolean isHtml) {
        final String confirmationToken = ConfirmationTokenUtil.generateToken();
        String from = null;
        Session session = null;

        try {
            /*
             * Optional credentials and smtp server.
             * If they are not provided or the smtp server name is not found in the configuration,
             * then the default credentials (config vars) and default smtp server will be used.
             */
            if (areCredentialsProvided(credentials) && smtpService.smtpServerByNameExists(credentials.getSmtpServerName())) {
                from = credentials.getUsername();
                session = smtpService.createSMTPSession(
                        credentials.getUsername(), credentials.getPassword(), credentials.getSmtpServerName());
            } else {
                from = smtpService.getUsername();
                session = smtpService.createSMTPSession();
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            if (isHtml) {
                message.setContent(content, "text/html");
            } else {
                message.setText(content);
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            emailHistoryService.persistSentEmail(from, to, subject, content, false, confirmationToken);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.MESSAGING);

            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            emailHistoryService.persistSentEmail(from, to, subject, content, false, confirmationToken);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.RUNTIME);

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            emailHistoryService.persistSentEmail(from, to, subject, content, false, confirmationToken);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.UNKNOWN);

            throw new RuntimeException(e);
        }

        emailHistoryService.persistSentEmail(from, to, subject, content, true, confirmationToken);
    }

    private List<RecipientEmailPreview> buildPreviewEmails(PreviewEmailResource previewEmailResource) {
        List<RecipientEmailPreview> recipientEmailPreviews = new ArrayList<>();

        for (Recipient recipient : previewEmailResource.getRecipients()) {
            RecipientEmailPreview recipientEmailPreview = RecipientEmailPreview.builder()
                    .email(recipient.getEmail())
                    .subject(previewEmailResource.getTitle())
                    .message(
                            EmailMessageUtil.buildEmailMessage(
                                    previewEmailResource.getMessage(),
                                    recipient.getPlaceholders(),
                                    emailTemplatesConfiguration.getPlaceholderPrefix(),
                                    emailTemplatesConfiguration.getPlaceholderSuffix()))
                    .build();

            recipientEmailPreviews.add(recipientEmailPreview);
        }

        return recipientEmailPreviews;
    }

    private boolean areCredentialsProvided(CredentialsResource credentialsResource) {
        return credentialsResource != null &&
               credentialsResource.getUsername() != null &&
               credentialsResource.getPassword() != null &&
               credentialsResource.getSmtpServerName() != null;
    }
}
