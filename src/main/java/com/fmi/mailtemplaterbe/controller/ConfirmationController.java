package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.config.ConfirmationConfiguration;
import com.fmi.mailtemplaterbe.domain.resource.ConfirmationEmailResource;
import com.fmi.mailtemplaterbe.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConfirmationController {

    private final ConfirmationService confirmationService;
    private final ConfirmationConfiguration confirmationConfiguration;

    @PatchMapping(value = "/confirmation/confirm")
    public ResponseEntity<Void> confirmSentEmail(
            @RequestBody ConfirmationEmailResource confirmationEmail) {
        confirmationService.confirmSentEmail(confirmationEmail);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/confirmation/sent-reply-email")
    public ResponseEntity<Void> sentEmailConfirmationReply (
            @RequestBody ConfirmationEmailResource confirmationEmail) {
        confirmationService.confirmSentEmail(confirmationEmail);

        return ResponseEntity.ok().build();
    }

    @GetMapping(
            value = "/confirmation/reply-message-max-length",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getReplyMessageMaxLength() {
        return ResponseEntity.ok(confirmationConfiguration.getReplyMessageMaxLength());
    }
}
