package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConfirmationController {

    private final ConfirmationService confirmationService;

    @PatchMapping(value = "/confirmation/confirm")
    public ResponseEntity<Void> confirmSentEmail(
            @RequestParam() String recipientEmail,
            @RequestParam() String recipientToken,
            @RequestParam() Long recipientConfirmation) {
        confirmationService.confirmSentEmail(recipientEmail, recipientToken, recipientConfirmation);

        return ResponseEntity.ok().build();
    }
}
