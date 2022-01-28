package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.config.EmailConfiguration;
import com.fmi.mailtemplaterbe.domain.resource.RecipientEmailPreview;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.domain.resource.SendEmailResource;
import com.fmi.mailtemplaterbe.util.EmailMessageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${username}")
    private String username;

    private final EmailConfiguration emailConfiguration;
    private final EmailHistoryService emailHistoryService;

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

    private int sendEmailToRecipients(SendEmailResource sendEmailResource) {
        int errorsCount = 0;

        for (RecipientResource recipient : sendEmailResource.getRecipients()) {
            try {
                final String emailSubject = sendEmailResource.getTitle();
                final String emailMessage =
                        EmailMessageUtil.buildEmailMessage(sendEmailResource.getMessage(), recipient.getPlaceholders());
                final boolean isHtmlMessage = sendEmailResource.getIsHtml();

                sendEmailToRecipient(username, recipient.getEmail(), emailSubject, emailMessage, isHtmlMessage);
            } catch (Exception e) {
                errorsCount++;
            }
        }

        return sendEmailResource.getRecipients().size() - errorsCount;
    }

    private void sendEmailToRecipient(String from, String to, String subject, String content, boolean isHtml) {
        try {
            Message message = new MimeMessage(emailConfiguration.createSMTPSession());
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
            emailHistoryService.persistSendEmailError(from, to, subject, content, e.getMessage());

            throw new RuntimeException(e);
        }

        emailHistoryService.persistSentEmail(from, to, subject, content, true);
    }

    private List<RecipientEmailPreview> buildPreviewEmails(SendEmailResource sendEmailResource) {
        List<RecipientEmailPreview> recipientEmailPreviews = new ArrayList<>();

        for (RecipientResource recipient : sendEmailResource.getRecipients()) {
            RecipientEmailPreview recipientEmailPreview = RecipientEmailPreview.builder()
                    .email(recipient.getEmail())
                    .subject(sendEmailResource.getTitle())
                    .message(EmailMessageUtil.buildEmailMessage(sendEmailResource.getMessage(), recipient.getPlaceholders()))
                    .build();

            recipientEmailPreviews.add(recipientEmailPreview);
        }

        return recipientEmailPreviews;
    }
}
