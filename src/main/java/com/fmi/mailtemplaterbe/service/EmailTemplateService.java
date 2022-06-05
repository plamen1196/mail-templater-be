package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.EmailTemplateEntity;
import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;
import com.fmi.mailtemplaterbe.repository.EmailTemplateRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import com.fmi.mailtemplaterbe.mapper.EmailTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.dao.DataIntegrityViolationException;
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
        EmailTemplateEntity savedEmailTemplateEntity = saveEmailTemplateEntity(emailTemplateEntity);

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
     * @param emailTemplateResource The resource data to use for the update.
     * @return The updated email template.
     */
    public EmailTemplateResource updateTemplateById(Long id, EmailTemplateResource emailTemplateResource) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(id).orElse(null);

        if (emailTemplateEntity == null) {
            throw ExceptionsUtil.getEmailTemplateNotFoundException(id);
        }

        return EmailTemplateMapper.entityToResource(
                updateTemplateEntityIfNecessary(emailTemplateEntity, emailTemplateResource));
    }

    /**
     * Delete an email template by its id.
     *
     * @param id The id of the email template.
     */
    public void deleteTemplateById(Long id) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(id).orElse(null);

        if (emailTemplateEntity == null) {
            throw ExceptionsUtil.getEmailTemplateNotFoundException(id);
        }

        emailTemplateRepository.delete(emailTemplateEntity);
    }

    /**
     * Returns true if an email template with the provided id exists.
     *
     * @param id The id of the email template.
     * @return True if the email template exists and false otherwise.
     */
    public boolean emailTemplateExistsById(Long id) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findById(id).orElse(null);

        return emailTemplateEntity != null;
    }

    private EmailTemplateEntity saveEmailTemplateEntity(EmailTemplateEntity emailTemplateEntity) {
        EmailTemplateEntity savedEmailTemplateEntity = null;

        try {
            savedEmailTemplateEntity = emailTemplateRepository.save(emailTemplateEntity);
        } catch (DataIntegrityViolationException e) {
            final Throwable dataIntegrityViolationCause = e.getCause();

            if (dataIntegrityViolationCause instanceof ConstraintViolationException) {
                final Throwable constraintViolationCause = dataIntegrityViolationCause.getCause();

                throw ExceptionsUtil.getEmailTemplateConstraintViolationException(
                        constraintViolationCause.getMessage());
            } else if (dataIntegrityViolationCause instanceof DataException) {
                final Throwable constraintViolationCause = dataIntegrityViolationCause.getCause();

                throw ExceptionsUtil.getCustomBadRequestException(constraintViolationCause.getMessage());
            }

            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return savedEmailTemplateEntity;
    }

    private EmailTemplateEntity updateTemplateEntityIfNecessary(
            EmailTemplateEntity emailTemplateEntity, EmailTemplateResource emailTemplateResource) {
        final String title = emailTemplateResource.getTitle();
        final String message = emailTemplateResource.getMessage();
        final List<String> placeholders = emailTemplateResource.getPlaceholders();

        if (StringUtils.isNotEmpty(title)) {
            emailTemplateEntity.setTitle(title);
        }

        if (StringUtils.isNotEmpty(message)) {
            emailTemplateEntity.setMessage(message);
        }

        if (placeholders != null) {
            emailTemplateEntity.setPlaceholders(EmailTemplateMapper.parsePlaceholdersToString(placeholders));
        }

        return saveEmailTemplateEntity(emailTemplateEntity);
    }

    private List<EmailTemplateResource> emailTemplateEntitiesToEmailTemplateResources(
            List<EmailTemplateEntity> mailTemplateEntities) {
        return mailTemplateEntities.stream()
                .map(EmailTemplateMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
