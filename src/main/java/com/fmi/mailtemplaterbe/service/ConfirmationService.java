package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.enums.SentEmailConfirmation;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationService {

    private final EmailHistoryService emailHistoryService;

    public void confirmSentEmail(
            String recipientEmail, String recipientConfirmationToken, Long recipientConfirmation) {
        SentEmailResource sentEmail = emailHistoryService.getSentEmail(recipientEmail, recipientConfirmationToken);

        if (sentEmail == null) {
            throw ExceptionsUtil.getSentEmailByRecipientEmailAndConfirmationTokenNotFoundException(
                    recipientEmail, recipientConfirmationToken);
        }

        SentEmailConfirmation recipientSentEmailConfirmation = SentEmailConfirmation.fromValue(recipientConfirmation);

        if (recipientSentEmailConfirmation == null) {
            throw ExceptionsUtil.getCustomBadRequestException("Invalid value for recipientConfirmation.");
        }

        emailHistoryService.confirmSentEmail(sentEmail.getId(), recipientSentEmailConfirmation);
    }
}
