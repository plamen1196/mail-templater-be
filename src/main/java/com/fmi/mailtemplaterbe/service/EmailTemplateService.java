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
        EmailTemplateEntity emailTemplateEntity = EmailTemplateMapper.resourceToEntity(emailTemplateResource);
        EmailTemplateEntity savedEmailTemplateEntity = emailTemplateRepository.save(emailTemplateEntity);

        return EmailTemplateMapper.entityToResource(savedEmailTemplateEntity);
    }

    /**
     * Get all email templates.
     *
     * @return {@link List<EmailTemplateResource>}
     */
    public List<EmailTemplateResource> getAllTemplates() {
        return emailTemplateEntitiesToEmailTemplateResources(emailTemplateRepository.findAll());
    }

    /**
     * Update an email template by its id.
     *
     * @param id The id of the email template.
     * @return The updated email template.
     */
    public EmailTemplateResource updateTemplateById(Long id) {

        return null;
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

    private List<EmailTemplateResource> emailTemplateEntitiesToEmailTemplateResources(
            List<EmailTemplateEntity> mailTemplateEntities) {
        return mailTemplateEntities.stream()
                .map(EmailTemplateMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
