package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.MailTemplateEntity;
import com.fmi.mailtemplaterbe.repository.MailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailTemplateService {

    private final MailTemplateRepository mailTemplateRepository;

    public List<MailTemplateEntity> getAllTemplates() {
        return mailTemplateRepository.findAll();
    }
}
