package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.RecipientEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.repository.RecipientEntityRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import com.fmi.mailtemplaterbe.mapper.RecipientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientEntityRepository recipientEntityRepository;

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
     * @param id The id of the recipient.
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
    }

    private RecipientEntity updateRecipientEntityIfNecessary(
            RecipientEntity recipientEntity, RecipientResource recipientResource) {
        final String email = recipientResource.getEmail();

        if (email != null) {
            recipientEntity.setEmail(email);
        }

        return recipientEntityRepository.save(recipientEntity);
    }

    private List<RecipientResource> recipientEntitiesToRecipientResources(List<RecipientEntity> recipientEntities) {
        return recipientEntities.stream()
                .map(RecipientMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
