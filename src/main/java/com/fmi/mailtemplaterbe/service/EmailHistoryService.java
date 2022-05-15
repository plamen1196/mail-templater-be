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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
                .timestamp(LocalDateTime.now())
                /* Confirmation is not set during persist, since it is updated separately from the client. */
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
                .timestamp(LocalDateTime.now())
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

    /**
     * Get a list with information about all sent emails in a specific date range.
     *
     * @param startDate Start date of the date range
     * @param endDate End date of the date range
     * @return list with information about all sent emails in specific date range
     */
    public List<SentEmailResource> getSentEmailsInRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<SentEmailEntity> sentEmailsInRange = new ArrayList<>();

        if (startDate != null && endDate != null) {
            sentEmailsInRange = getSentEmailEntitiesBetweenDates(startDate, endDate);
        }

        if (startDate != null && endDate == null) {
            sentEmailsInRange = getSentEmailEntitiesAfterDate(startDate);
        }

        if (startDate == null && endDate != null) {
            sentEmailsInRange = getSentEmailEntitiesBeforeDate(endDate);
        }

        return sentEmailEntitiesToSentEmailResource(sentEmailsInRange);
    }

    private List<SentEmailEntity> getSentEmailEntitiesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime pureStartDate = LocalDateTime.of(startDate.toLocalDate(), LocalTime.MIN);
        LocalDateTime pureEndDate = LocalDateTime.of(endDate.toLocalDate(), LocalTime.MAX);

        return sentEmailEntityRepository.findAllByTimestampBetween(pureStartDate, pureEndDate).orElse(null);
    }

    private List<SentEmailEntity> getSentEmailEntitiesAfterDate(LocalDateTime date) {
        LocalDateTime pureEndDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MAX);

        return sentEmailEntityRepository.findAllByTimestampAfter(pureEndDate).orElse(null);
    }

    private List<SentEmailEntity> getSentEmailEntitiesBeforeDate(LocalDateTime date) {
        LocalDateTime pureEndDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MIN);

        return sentEmailEntityRepository.findAllByTimestampBefore(pureEndDate).orElse(null);
    }

    private List<SentEmailResource> sentEmailEntitiesToSentEmailResource(List<SentEmailEntity> sentEmailEntities) {
        return sentEmailEntities.stream()
                .map(SentEmailMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
