package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.EmailTemplateEntity;
import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;
import com.fmi.mailtemplaterbe.repository.EmailTemplateRepository;
import com.fmi.mailtemplaterbe.util.EmailTemplateExceptionsUtil;
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
    public EmailTemplateResource updateTemplateById(Long id, EmailTemplateResource emailTemplateResource) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(id).orElse(null);

        if (emailTemplateEntity == null) {
            throw EmailTemplateExceptionsUtil.getEmailTemplateNotFoundException(id);
        }

        return EmailTemplateMapper.entityToResource(updateTemplateEntityIfNecessary(emailTemplateEntity, emailTemplateResource));
    }

    /**
     * Delete an email template by its id.
     *
     * @param id The id of the email template.
     */
    public void deleteTemplateById(Long id) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(id).orElse(null);

        if (emailTemplateEntity == null) {
            throw EmailTemplateExceptionsUtil.getEmailTemplateNotFoundException(id);
        }

        emailTemplateRepository.delete(emailTemplateEntity);
    }

    private EmailTemplateEntity updateTemplateEntityIfNecessary(EmailTemplateEntity emailTemplateEntity, EmailTemplateResource emailTemplateResource) {
        final String title = emailTemplateResource.getTitle();
        final String message = emailTemplateResource.getMessage();
        final List<String> placeholders = emailTemplateResource.getPlaceholders();

        if (title != null) {
            emailTemplateEntity.setTitle(title);
        }

        if (message != null) {
            emailTemplateEntity.setMessage(message);
        }

        if (placeholders != null) {
            emailTemplateEntity.setPlaceholders(EmailTemplateMapper.parsePlaceholdersToString(placeholders));
        }

        return emailTemplateRepository.save(emailTemplateEntity);
    }

    private List<EmailTemplateResource> emailTemplateEntitiesToEmailTemplateResources(
            List<EmailTemplateEntity> mailTemplateEntities) {
        return mailTemplateEntities.stream()
                .map(EmailTemplateMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
