package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;
import com.fmi.mailtemplaterbe.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class EmailTemplatesController {

    private final EmailTemplateService emailTemplateService;

    @PostMapping(
            value = "/templates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTemplateResource> createTemplate(
            @Valid @RequestBody EmailTemplateResource emailTemplateResource) {
        return ResponseEntity.ok(emailTemplateService.createTemplate(emailTemplateResource));
    }

    @GetMapping(
            value = "/templates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmailTemplateResource>> getTemplates() {
        return ResponseEntity.ok(emailTemplateService.getAllTemplates());
    }

    @PatchMapping(
            value = "/templates/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTemplateResource> updateTemplate(
            @PathVariable(value = "id") Long id, @RequestBody EmailTemplateResource emailTemplateResource) {
        return ResponseEntity.ok(emailTemplateService.updateTemplateById(id, emailTemplateResource));
    }

    @DeleteMapping(
            value = "/templates/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTemplateResource> deleteTemplate(@PathVariable(value = "id") Long id) {
        emailTemplateService.deleteTemplateById(id);

        return ResponseEntity.ok().build();
    }
}
