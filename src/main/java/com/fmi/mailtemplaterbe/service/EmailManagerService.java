package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.EmailTemplatesConfiguration;
import com.fmi.mailtemplaterbe.domain.enums.EmailErrorCategory;
import com.fmi.mailtemplaterbe.domain.resource.RecipientEmailPreview;
import com.fmi.mailtemplaterbe.domain.resource.Recipient;
import com.fmi.mailtemplaterbe.domain.resource.SendEmailResource;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
import com.fmi.mailtemplaterbe.util.EmailMessageUtil;
import com.fmi.mailtemplaterbe.util.SentEmailsLocalDateTimeComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailManagerService {

    private final EmailTemplatesConfiguration emailTemplatesConfiguration;
    private final EmailHistoryService emailHistoryService;

    private final SmtpService smtpService;

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
     * @param sendEmailResource SendEmailResource
     * @return list of preview emails
     */
    public List<RecipientEmailPreview> getPreviewEmails(SendEmailResource sendEmailResource) {
        return buildPreviewEmails(sendEmailResource);
    }

    /**
     * Get a list with information about all sent emails - full history.
     *
     * @return list with information about all sent emails
     */
    public List<SentEmailResource> getSentEmails(LocalDateTime startDate, LocalDateTime endDate) {
        List<SentEmailResource> sentEmails;

        if (startDate != null || endDate != null) {
            sentEmails = emailHistoryService.getSentEmailsInRange(startDate, endDate);
        } else {
            sentEmails = emailHistoryService.getAllSentEmails();
        }

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
                final String sender = smtpService.getUsername();

                sendEmailToRecipient(sender, recipient.getEmail(), emailSubject, emailMessage, isHtmlMessage);
            } catch (Exception e) {
                errorsCount++;
            }
        }

        return sendEmailResource.getRecipients().size() - errorsCount;
    }

    private void sendEmailToRecipient(String from, String to, String subject, String content, boolean isHtml) {
        try {
            Message message = new MimeMessage(smtpService.createSMTPSession());
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
            emailHistoryService.persistSentEmail(from, to, subject, content, false);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.MESSAGING);

            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            emailHistoryService.persistSentEmail(from, to, subject, content, false);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.RUNTIME);

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            emailHistoryService.persistSentEmail(from, to, subject, content, false);
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage(), EmailErrorCategory.UNKNOWN);

            throw new RuntimeException(e);
        }

        emailHistoryService.persistSentEmail(from, to, subject, content, true);
    }

    private List<RecipientEmailPreview> buildPreviewEmails(SendEmailResource sendEmailResource) {
        List<RecipientEmailPreview> recipientEmailPreviews = new ArrayList<>();

        for (Recipient recipient : sendEmailResource.getRecipients()) {
            RecipientEmailPreview recipientEmailPreview = RecipientEmailPreview.builder()
                    .email(recipient.getEmail())
                    .subject(sendEmailResource.getTitle())
                    .message(
                            EmailMessageUtil.buildEmailMessage(
                                    sendEmailResource.getMessage(),
                                    recipient.getPlaceholders(),
                                    emailTemplatesConfiguration.getPlaceholderPrefix(),
                                    emailTemplatesConfiguration.getPlaceholderSuffix()))
                    .build();

            recipientEmailPreviews.add(recipientEmailPreview);
        }

        return recipientEmailPreviews;
    }
}
