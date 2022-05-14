package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.RecipientEmailPreview;
import com.fmi.mailtemplaterbe.domain.resource.SendEmailResource;
import com.fmi.mailtemplaterbe.domain.resource.SentEmailResource;
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
    public ResponseEntity<List<RecipientEmailPreview>> getPreviewEmails(@Valid @RequestBody SendEmailResource sendEmailResource) {
        return ResponseEntity.ok(emailManagerService.getPreviewEmails(sendEmailResource));
    }

    @GetMapping(
            value = "/history",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SentEmailResource>> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(emailManagerService.getSentEmails(startDate, endDate));
    }
}
