package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.*;
import com.fmi.mailtemplaterbe.service.EmailManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmailManagerController {

    private final EmailManagerService emailManagerService;

    @GetMapping(
            value = "/default-smtp-server",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmtpServerResource> getDefaultSmtpServer() {
        return ResponseEntity.ok(emailManagerService.getDefaultSmtpServer());
    }

    @PostMapping(
            value = "/send-emails",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> sendEmail(@Valid @RequestBody SendEmailResource sendEmailResource) {
        return ResponseEntity.ok(emailManagerService.sendEmails(sendEmailResource));
    }

    @PostMapping(
            value = "/preview-emails",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientEmailPreview>> getPreviewEmails(@Valid @RequestBody PreviewEmailResource previewEmailResource) {
        return ResponseEntity.ok(emailManagerService.getPreviewEmails(previewEmailResource));
    }

    @GetMapping(
            value = "/history",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SentEmailResource>> getHistory(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String senderEmail,
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(required = false) Boolean sentSuccessfully,
            @RequestParam(required = false) Long confirmation,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(emailManagerService.getSentEmails(
                subject, senderEmail, recipientEmail, sentSuccessfully, confirmation, startDate, endDate));
    }
}
