package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.enums.SentEmailConfirmation;
import com.fmi.mailtemplaterbe.domain.resource.ConfirmationEmailResource;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationService {

    private final EmailHistoryService emailHistoryService;
    private final EmailManagerService emailManagerService;

    public void confirmSentEmail(ConfirmationEmailResource confirmationEmail) {
        SentEmailResource sentEmail = emailHistoryService.getSentEmail(
                confirmationEmail.getRecipientEmail(),
                confirmationEmail.getRecipientToken());

        if (sentEmail == null) {
            throw ExceptionsUtil.getSentEmailByRecipientEmailAndConfirmationTokenNotFoundException(
                    confirmationEmail.getRecipientEmail(),
                    confirmationEmail.getRecipientToken());
        }

        SentEmailConfirmation recipientSentEmailConfirmation = SentEmailConfirmation.fromValue(
                confirmationEmail.getRecipientConfirmation());

        if (recipientSentEmailConfirmation == null) {
            throw ExceptionsUtil.getCustomBadRequestException("Invalid value for recipientConfirmation.");
        }

        emailHistoryService.confirmSentEmail(sentEmail.getId(), recipientSentEmailConfirmation);
    }
}
