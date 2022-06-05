package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.SendEmailErrorEntity;
import com.fmi.mailtemplaterbe.domain.entity.SentEmailEntity;
import com.fmi.mailtemplaterbe.domain.enums.EmailErrorCategory;
import com.fmi.mailtemplaterbe.domain.enums.SentEmailConfirmation;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
import com.fmi.mailtemplaterbe.mapper.SentEmailMapper;
import com.fmi.mailtemplaterbe.repository.SendEmailErrorRepository;
import com.fmi.mailtemplaterbe.repository.SentEmailEntityRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
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
     * @param emailTemplateId  Id of the email template that was used for the email.
     * @param sender           Email address of the sender.
     * @param recipient        Email address of the recipient.
     * @param subject          Subject of the email.
     * @param message          Message of the email.
     * @param sentSuccessfully True if the email was sent successfully and false otherwise.
     * @param token            Confirmation token for the sent email.
     * @param sendEmailErrorId Id of the email error that occurred. If no error was thrown, pass null.
     * @return Saved record for the sent email.
     */
    public SentEmailEntity persistSentEmail(
            Long emailTemplateId,
            String sender,
            String recipient,
            String subject,
            String message,
            boolean sentSuccessfully,
            String token,
            Long sendEmailErrorId) {
        SentEmailEntity sentEmailEntity = SentEmailEntity.builder()
                .emailTemplateId(emailTemplateId)
                .senderEmail(sender)
                .recipientEmail(recipient)
                .subject(subject)
                .message(message)
                .sentSuccessfully(sentSuccessfully)
                .sendEmailErrorId(sendEmailErrorId != null ? sendEmailErrorId : null)
                .timestamp(LocalDateTime.now())
                /* Initial confirmation is always unconfirmed. */
                .confirmation(SentEmailConfirmation.UNCONFIRMED.getValue())
                .token(token)
                .build();

        return sentEmailEntityRepository.save(sentEmailEntity);
    }

    /**
     * Persist the information of an email that failed to send.
     * The method is used only for not successfully sent emails.
     *
     * @param sender    Email address of the sender.
     * @param recipient Email address of the recipient.
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
        List<SentEmailResource> sentEmails;

        /* First, filter by date, because date filtering has multiple options. */
        if (startDate != null || endDate != null) {
            sentEmails = getSentEmailsInRange(startDate, endDate);
        } else {
            sentEmails = getAllSentEmails();
        }

        /* Second, filter by the rest of the fields. */
        sentEmails = filterSentEmailsBySubject(sentEmails, subject);
        sentEmails = filterSentEmailsBySenderEmail(sentEmails, senderEmail);
        sentEmails = filterSentEmailsByRecipientEmail(sentEmails, recipientEmail);
        sentEmails = filterSentEmailsBySentSuccessfully(sentEmails, sentSuccessfully);
        sentEmails = filterSentEmailsByConfirmation(sentEmails, confirmation);

        /* Third, apply the errorMessage for the sent emails. */
        sentEmails = applyErrorMessageIfErrorExists(sentEmails);

        return sentEmails;
    }

    /**
     * Get a sent email by the recipient email address and the confirmation token.
     * If no such sent email was found, then null is returned.
     *
     * @param recipientEmail Recipient email address
     * @param confirmationToken Confirmation token
     * @return sent email if found, otherwise null
     */
    public SentEmailResource getSentEmail(String recipientEmail, String confirmationToken) {
        return SentEmailMapper.entityToResource(
                sentEmailEntityRepository.findByRecipientEmailAndToken(recipientEmail, confirmationToken)
                        .orElse(null));
    }

    /**
     * Confirm a sent email by updating its confirmation field to the passed value.
     *
     * @param id The id of the sent email
     * @param sentEmailConfirmation The confirmation value for the sent email
     * @return Updated sent email
     */
    public SentEmailResource confirmSentEmail(Long id, SentEmailConfirmation sentEmailConfirmation) {
        if (sentEmailConfirmation == null) {
            throw new IllegalArgumentException("Missing confirmation value");
        }

        SentEmailEntity sentEmailEntity = sentEmailEntityRepository.findById(id).orElse(null);

        if (sentEmailEntity == null) {
            throw ExceptionsUtil.getSentEmailByIdNotFoundException(id);
        }

        if (SentEmailConfirmation.fromValue(sentEmailEntity.getConfirmation()) != SentEmailConfirmation.UNCONFIRMED) {
            throw ExceptionsUtil.getSentEmailAlreadyConfirmedException(
                    sentEmailEntity.getRecipientEmail(), sentEmailEntity.getToken());
        }

        sentEmailEntity.setConfirmation(sentEmailConfirmation.getValue());

        return SentEmailMapper.entityToResource(sentEmailEntityRepository.save(sentEmailEntity));
    }

    private List<SentEmailResource> getAllSentEmails() {
        return sentEmailEntitiesToSentEmailResource(sentEmailEntityRepository.findAll());
    }

    private List<SentEmailResource> getSentEmailsInRange(LocalDateTime startDate, LocalDateTime endDate) {
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

    private List<SentEmailResource> filterSentEmailsBySubject(
            List<SentEmailResource> sentEmails, String subject) {
        if (subject == null) {
            return sentEmails;
        }

        return sentEmails.stream()
                .filter(sentEmail -> sentEmail.getSubject().toLowerCase().contains(subject.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<SentEmailResource> filterSentEmailsBySenderEmail(
            List<SentEmailResource> sentEmails, String senderEmail) {
        if (senderEmail == null) {
            return sentEmails;
        }

        return sentEmails.stream()
                .filter(sentEmail -> sentEmail.getSenderEmail().toLowerCase().contains(senderEmail.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<SentEmailResource> filterSentEmailsByRecipientEmail(
            List<SentEmailResource> sentEmails, String recipientEmail) {
        if (recipientEmail == null) {
            return sentEmails;
        }

        return sentEmails.stream()
                .filter(sentEmail -> sentEmail.getRecipientEmail().toLowerCase().contains(recipientEmail.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<SentEmailResource> filterSentEmailsBySentSuccessfully(
            List<SentEmailResource> sentEmails, Boolean sentSuccessfully) {
        if (sentSuccessfully == null) {
            return sentEmails;
        }

        return sentEmails.stream()
                .filter(sentEmail -> sentSuccessfully.equals(sentEmail.isSentSuccessfully()))
                .collect(Collectors.toList());
    }

    private List<SentEmailResource> filterSentEmailsByConfirmation(
            List<SentEmailResource> sentEmails, Long confirmation) {
        if (confirmation == null) {
            return sentEmails;
        }

        if (SentEmailConfirmation.fromValue(confirmation) == null) {
            throw new IllegalArgumentException("Invalid confirmation value");
        }

        return sentEmails.stream()
                .filter(sentEmail -> confirmation.equals(sentEmail.getConfirmation()))
                .collect(Collectors.toList());
    }

    private List<SentEmailResource> applyErrorMessageIfErrorExists(List<SentEmailResource> sentEmails) {
        for (SentEmailResource sentEmail : sentEmails) {
            SentEmailEntity sentEmailEntity = sentEmailEntityRepository.findById(sentEmail.getId()).orElse(null);

            if (sentEmailEntity != null) {
                Long sendEmailErrorId = sentEmailEntity.getSendEmailErrorId();

                if (sendEmailErrorId != null) {
                    SendEmailErrorEntity sendEmailErrorEntity = sendEmailErrorRepository.findById(sendEmailErrorId).orElse(null);

                    if (sendEmailErrorEntity != null) {
                        /* Set error message if such exists */
                        sentEmail.setErrorMessage(sendEmailErrorEntity.getError());
                    }
                }
            }
        }

        return sentEmails;
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
