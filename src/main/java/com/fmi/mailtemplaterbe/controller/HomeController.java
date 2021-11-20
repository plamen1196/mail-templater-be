package com.fmi.mailtemplaterbe.controller;

import com.fmi.mailtemplaterbe.domain.entity.MailTemplateEntity;
import com.fmi.mailtemplaterbe.repository.MailTemplateRepository;
import com.fmi.mailtemplaterbe.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private final MailTemplateService mailTemplateService;

    @GetMapping(value = "/home")
    public ResponseEntity<String> hello() {
        List<MailTemplateEntity> result = mailTemplateService.getAllTemplates();

        return ResponseEntity.ok("Templates fetched");
    }
}
