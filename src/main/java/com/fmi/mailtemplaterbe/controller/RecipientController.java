package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecipientController {

    private final RecipientService recipientService;

    @PostMapping(
            value = "/recipients",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientResource> createRecipient(
            @Valid @RequestBody RecipientResource recipientResource) {
        return ResponseEntity.ok(recipientService.createRecipient(recipientResource));
    }

    @GetMapping(
            value = "/recipients",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipientResource>> getRecipients() {
        return ResponseEntity.ok(recipientService.getAllRecipients());
    }

    @PatchMapping(
            value = "/recipients/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientResource> createRecipient(
            @PathVariable(value = "id") Long id, @RequestBody RecipientResource recipientResource) {
        return ResponseEntity.ok(recipientService.updateRecipientById(id, recipientResource));
    }

    @DeleteMapping(
            value = "/recipients/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipientResource> deleteRecipient(@PathVariable(value = "id") Long id) {
        recipientService.deleteRecipientById(id);

        return ResponseEntity.ok().build();
    }
}
