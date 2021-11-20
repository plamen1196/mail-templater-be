package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;
import com.fmi.mailtemplaterbe.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmailTemplatesController {

    private final EmailTemplateService emailTemplateService;

    @PostMapping(
            value = "/templates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTemplateResource> createTemplate(@RequestBody EmailTemplateResource emailTemplateResource) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(
            value = "/templates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmailTemplateResource>> getTemplates() {
        return ResponseEntity.ok(emailTemplateService.getAllTemplates());
    }

    @DeleteMapping(
            value = "/templates/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTemplateResource> deleteTemplate(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(emailTemplateService.deleteTemplateById(Long.valueOf(id)));
    }


}
