package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.RecipientEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.repository.RecipientEntityRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import com.fmi.mailtemplaterbe.mapper.RecipientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientEntityRepository recipientEntityRepository;
    private final RecipientGroupCleanupService recipientGroupCleanupService;

    /**
     * Create a recipient.
     *
     * @param recipientResource The recipient for creation.
     * @return The created recipient.
     */
    public RecipientResource createRecipient(RecipientResource recipientResource) {
        RecipientEntity recipientEntity = RecipientMapper.resourceToEntity(recipientResource);
        RecipientEntity savedRecipientEntity = recipientEntityRepository.save(recipientEntity);

        return RecipientMapper.entityToResource(savedRecipientEntity);
    }

    /**
     * Get all recipients.
     *
     * @return {@link List<RecipientResource>}
     */
    public List<RecipientResource> getAllRecipients() {
        return recipientEntitiesToRecipientResources(recipientEntityRepository.findAll());
    }

    /**
     * Update the recipient by its id.
     *
     * @param id                The id of the recipient.
     * @param recipientResource The resource data to use for the update.
     * @return The updated recipient.
     */
    public RecipientResource updateRecipientById(Long id, RecipientResource recipientResource) {
        RecipientEntity recipientEntity = recipientEntityRepository.findById(id).orElse(null);

        if (recipientEntity == null) {
            throw ExceptionsUtil.getRecipientNotFoundException(id);
        }

        return RecipientMapper.entityToResource(updateRecipientEntityIfNecessary(recipientEntity, recipientResource));
    }

    /**
     * Delete a recipient by its id.
     *
     * @param id The id of the recipient.
     */
    public void deleteRecipientById(Long id) {
        RecipientEntity recipientEntity = recipientEntityRepository.findById(id).orElse(null);

        if (recipientEntity == null) {
            throw ExceptionsUtil.getRecipientNotFoundException(id);
        }

        recipientEntityRepository.delete(recipientEntity);

        /* Remove the id of the deleted recipient from the recipientIds lists of all recipient groups. */
        recipientGroupCleanupService.removeRecipientFromAllRecipientGroups(id);
    }

    /**
     * Get a recipient by its id.
     *
     * @param id The id of the recipient.
     * @return recipient
     */
    public RecipientResource getRecipientById(Long id) {
        RecipientEntity recipientEntity = recipientEntityRepository.findById(id).orElse(null);

        if (recipientEntity == null) {
            throw ExceptionsUtil.getRecipientNotFoundException(id);
        }

        return RecipientMapper.entityToResource(recipientEntity);
    }

    /**
     * Get a recipient by its id.
     * <p></p>
     * NOTE: If the recipient is not found, then null will be returned.
     *
     * @param id The id of the recipient.
     * @return recipient
     */
    public RecipientResource getUncheckedRecipientById(Long id) {
        RecipientEntity recipientEntity = recipientEntityRepository.findById(id).orElse(null);

        if (recipientEntity == null) {
            return null;
        }

        return RecipientMapper.entityToResource(recipientEntity);
    }

    /**
     * Get all recipients, whose id is in the provided recipientIds list.
     *
     * @param recipientIds Ids of the recipients
     * @return recipients
     */
    public List<RecipientResource> getRecipientsByIds(List<Long> recipientIds) {
        List<RecipientEntity> recipientEntities = recipientEntityRepository.findByIdIn(recipientIds).orElse(null);

        if (CollectionUtils.isEmpty(recipientEntities)) {
            return Collections.emptyList();
        }

        return recipientEntitiesToRecipientResources(recipientEntities);
    }

    private RecipientEntity updateRecipientEntityIfNecessary(
            RecipientEntity recipientEntity, RecipientResource recipientResource) {
        final String email = recipientResource.getEmail();
        final String firstName = recipientResource.getFirstName();
        final String lastName = recipientResource.getLastName();
        final String phoneNumber = recipientResource.getPhoneNumber();
        final String address = recipientResource.getAddress();

        if (StringUtils.isNotEmpty(email)) {
            recipientEntity.setEmail(email);
        }

        if (StringUtils.isNotEmpty(firstName)) {
            recipientEntity.setFirstName(firstName);
        }

        if (StringUtils.isNotEmpty(lastName)) {
            recipientEntity.setLastName(lastName);
        }

        if (StringUtils.isNotEmpty(phoneNumber)) {
            recipientEntity.setPhoneNumber(phoneNumber);
        }

        if (StringUtils.isNotEmpty(address)) {
            recipientEntity.setAddress(address);
        }

        return recipientEntityRepository.save(recipientEntity);
    }

    private List<RecipientResource> recipientEntitiesToRecipientResources(List<RecipientEntity> recipientEntities) {
        return recipientEntities.stream()
                .map(RecipientMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
