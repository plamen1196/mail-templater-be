package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.SendEmailErrorEntity;
import com.fmi.mailtemplaterbe.domain.entity.SentEmailEntity;
import com.fmi.mailtemplaterbe.domain.enums.EmailErrorCategory;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
import com.fmi.mailtemplaterbe.mapper.SentEmailMapper;
import com.fmi.mailtemplaterbe.repository.SendEmailErrorRepository;
import com.fmi.mailtemplaterbe.repository.SentEmailEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final SentEmailEntityRepository sentEmailEntityRepository;
    private final SendEmailErrorRepository sendEmailErrorRepository;

    /**
     * Persist the information of a sent email within the database for history purposes..
     * The method is used for both successfully and not successfully sent emails.
     *
     * @param sender           Email of the sender.
     * @param recipient        Email of the recipient.
     * @param subject          Subject of the email.
     * @param message          Message of the email.
     * @param sentSuccessfully True if the email was sent successfully and false otherwise.
     * @return Saved record for the sent email.
     */
    public SentEmailEntity persistSentEmail(
            String sender, String recipient, String subject, String message, boolean sentSuccessfully) {
        SentEmailEntity sentEmailEntity = SentEmailEntity.builder()
                .senderEmail(sender)
                .recipientEmail(recipient)
                .subject(subject)
                .message(message)
                .sentSuccessfully(sentSuccessfully)
                .build();

        return sentEmailEntityRepository.save(sentEmailEntity);
    }

    /**
     * Persist the information of an email that failed to send.
     * The method is used only for not successfully sent emails.
     *
     * @param sender    Email of the sender.
     * @param recipient Email of the recipient.
     * @param subject   Subject of the email.
     * @param message   Message of the email.
     * @param error     Error message.
     * @return Saved record for the failed email.
     */
    public SendEmailErrorEntity persistSendEmailError(
            String sender, String recipient, String subject, String message, String error, EmailErrorCategory emailErrorCategory) {
        SendEmailErrorEntity sendEmailErrorEntity = SendEmailErrorEntity.builder()
                .senderEmail(sender)
                .recipientEmail(recipient)
                .subject(subject)
                .message(message)
                .error(error != null ? error : "N/A")
                .category(emailErrorCategory != null ? emailErrorCategory.getValue() : EmailErrorCategory.UNKNOWN.getValue())
                .build();

        return sendEmailErrorRepository.save(sendEmailErrorEntity);
    }

    /**
     * Get a list with information about all sent emails - full history.
     *
     * @return list with information about all sent emails
     */
    public List<SentEmailResource> getAllSentEmails() {
        return sentEmailEntitiesToSentEmailResource(sentEmailEntityRepository.findAll());
    }

    private List<SentEmailResource> sentEmailEntitiesToSentEmailResource(List<SentEmailEntity> sentEmailEntities) {
        return sentEmailEntities.stream()
                .map(SentEmailMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
