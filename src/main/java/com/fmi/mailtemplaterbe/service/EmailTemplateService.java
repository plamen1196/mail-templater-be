package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.EmailTemplateEntity;
import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;
import com.fmi.mailtemplaterbe.repository.EmailTemplateRepository;
import com.fmi.mailtemplaterbe.util.EmailTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    /**
     * Create an email template.
     *
     * @param emailTemplateResource The email template data for creation.
     * @return The crated email template.
     */
    public EmailTemplateResource createTemplate(EmailTemplateResource emailTemplateResource) {
        return null;
    }

    /**
     * Get all email templates.
     *
     * @return {@link List<EmailTemplateResource>}
     */
    public List<EmailTemplateResource> getAllTemplates() {
        return mailTemplateEntitiesToMailTemplateResources(emailTemplateRepository.findAll());
    }

    /**
     * Delete an email template by its id.
     *
     * @param id The id of the email template.
     * @return The deleted email template.
     */
    public EmailTemplateResource deleteTemplateById(Long id) {
        return null;
    }

    private List<EmailTemplateResource> mailTemplateEntitiesToMailTemplateResources(
            List<EmailTemplateEntity> mailTemplateEntities) {
        return mailTemplateEntities.stream()
                .map(EmailTemplateMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
