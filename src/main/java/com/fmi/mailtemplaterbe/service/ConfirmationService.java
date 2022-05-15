package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.enums.SentEmailConfirmation;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
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
            // todo: no sent email found with this recipientEmail and token
        }

        SentEmailConfirmation recipientSentEmailConfirmation = SentEmailConfirmation.fromValue(recipientConfirmation);

        if (recipientConfirmation == null) {
            // todo: invalid confirmation value
        }

        emailHistoryService.confirmSentEmail(sentEmail.getId(), recipientSentEmailConfirmation);
    }
}
