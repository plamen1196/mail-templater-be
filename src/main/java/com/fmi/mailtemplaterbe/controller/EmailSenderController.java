package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.RecipientEmailPreview;
import com.fmi.mailtemplaterbe.domain.resource.SendEmailResource;
import com.fmi.mailtemplaterbe.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    @PostMapping(
            value = "/send-emails",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> sendEmail(@Valid @RequestBody SendEmailResource sendEmailResource) {
        return ResponseEntity.ok(emailSenderService.sendEmails(sendEmailResource));
    }

    @PostMapping(
            value = "/preview-emails",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientEmailPreview>> getPreviewEmails(@Valid @RequestBody SendEmailResource sendEmailResource) {
        return ResponseEntity.ok(emailSenderService.getPreviewEmails(sendEmailResource));
    }
}
