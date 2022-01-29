package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.RecipientGroupEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientGroupResource;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.repository.RecipientGroupRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import com.fmi.mailtemplaterbe.mapper.RecipientGroupMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipientGroupService {

    private final RecipientGroupRepository recipientGroupRepository;
    private final RecipientService recipientService;

    /**
     * Create a recipient group.
     *
     * @param recipientGroupResource The recipient group data for creation.
     * @return The created recipient group.
     */
    public RecipientGroupResource createRecipientGroup(RecipientGroupResource recipientGroupResource) {
        RecipientGroupEntity recipientGroupEntity = RecipientGroupMapper.resourceToEntity(recipientGroupResource);
        RecipientGroupEntity savedRecipientGroupEntity = saveRecipientGroupEntity(recipientGroupEntity);

        return RecipientGroupMapper.entityToResource(savedRecipientGroupEntity);
    }


    /**
     * Get all recipient groups.
     *
     * @return {@link List<RecipientGroupResource>}
     */
    public List<RecipientGroupResource> getAllRecipientGroups() {
        return recipientGroupEntitiesToRecipientGroupResource(recipientGroupRepository.findAll());
    }

    /**
     * Update a recipient group by its id.
     *
     * @param id                     The id of the recipient group.
     * @param recipientGroupResource The resource data to use for the update.
     * @return The updated recipient group.
     */
    public RecipientGroupResource updateRecipientGroupById(Long id, RecipientGroupResource recipientGroupResource) {
        RecipientGroupEntity recipientGroupEntity = recipientGroupRepository.findById(id).orElse(null);

        if (recipientGroupEntity == null) {
            throw ExceptionsUtil.getRecipientGroupNotFoundException(id);
        }

        return RecipientGroupMapper.entityToResource(
                updateRecipientGroupEntityIfNecessary(recipientGroupEntity, recipientGroupResource));
    }

    /**
     * Delete a recipient group by its id.
     *
     * @param id The id of the recipient group.
     */
    public void deleteRecipientGroupById(Long id) {
        RecipientGroupEntity recipientGroupEntity = recipientGroupRepository.findById(id).orElse(null);

        if (recipientGroupEntity == null) {
            throw ExceptionsUtil.getRecipientGroupNotFoundException(id);
        }

        recipientGroupRepository.delete(recipientGroupEntity);
    }

    /**
     * Get all recipients of the recipient group with the provided id.
     *
     * @param id The id of the recipient group.
     * @return All recipients of the respective group.
     */
    public List<RecipientResource> getRecipientsOfRecipientGroupById(Long id) {
        RecipientGroupEntity recipientGroupEntity = recipientGroupRepository.findById(id).orElse(null);

        if (recipientGroupEntity == null) {
            throw ExceptionsUtil.getRecipientGroupNotFoundException(id);
        }

        List<Long> recipientIds =
                RecipientGroupMapper.parseRecipientIdsToList(recipientGroupEntity.getRecipientIds());

        return recipientService.getRecipientsByIds(recipientIds);
    }

    /**
     * Add a recipient to a recipient group based on the provided recipientGroupId and the recipientId.
     *
     * @param recipientGroupId The id of the recipient group.
     * @param recipientId      The id of the recipient.
     * @return Updated list of all recipients of the respective group.
     */
    public List<RecipientResource> addRecipientToRecipientGroup(Long recipientGroupId, Long recipientId) {
        RecipientGroupEntity recipientGroupEntity = recipientGroupRepository.findById(recipientGroupId).orElse(null);

        if (recipientGroupEntity == null) {
            throw ExceptionsUtil.getRecipientGroupNotFoundException(recipientGroupId);
        }

        /* Call this to make sure the recipient exists in the database. */
        RecipientResource recipient = recipientService.getRecipientById(recipientId);

        /* Add new recipient to list of recipient ids if not already present. */
        List<Long> recipientIds =
                RecipientGroupMapper.parseRecipientIdsToList(recipientGroupEntity.getRecipientIds());
        if (!recipientIds.contains(recipient.getId())) {
            recipientIds.add(recipient.getId());
        }

        /* Update and save the recipient group with the added recipient id. */
        String newRecipientIds = RecipientGroupMapper.parseRecipientIdsToString(recipientIds);
        recipientGroupEntity.setRecipientIds(newRecipientIds);
        recipientGroupRepository.save(recipientGroupEntity);

        return getRecipientsOfRecipientGroupById(recipientGroupId);
    }

    /**
     * Remove a recipient from a recipient group based on the provided recipientGroupId and the recipientId.
     *
     * @param recipientGroupId The id of the recipient group.
     * @param recipientId      The id of the recipient.
     * @return Updated list of all recipients of the respective group.
     */
    public List<RecipientResource> removeRecipientFromRecipientGroup(Long recipientGroupId, Long recipientId) {
        RecipientGroupEntity recipientGroupEntity = recipientGroupRepository.findById(recipientGroupId).orElse(null);

        if (recipientGroupEntity == null) {
            throw ExceptionsUtil.getRecipientGroupNotFoundException(recipientGroupId);
        }

        /* Call this to make sure the recipient exists in the database. */
        RecipientResource recipient = recipientService.getRecipientById(recipientId);

        /* Remove the recipient from the list of recipient ids if present. */
        List<Long> recipientIds =
                RecipientGroupMapper.parseRecipientIdsToList(recipientGroupEntity.getRecipientIds());
        if (recipientIds.contains(recipient.getId())) {
            recipientIds.remove(recipient.getId());
        }

        /* Update and save the recipient group with the added recipient id. */
        String newRecipientIds = RecipientGroupMapper.parseRecipientIdsToString(recipientIds);
        recipientGroupEntity.setRecipientIds(newRecipientIds);
        recipientGroupRepository.save(recipientGroupEntity);

        return getRecipientsOfRecipientGroupById(recipientGroupId);
    }

    private RecipientGroupEntity saveRecipientGroupEntity(RecipientGroupEntity recipientGroupEntity) {
        RecipientGroupEntity savedRecipientGroupEntity = null;

        try {
            savedRecipientGroupEntity = recipientGroupRepository.save(recipientGroupEntity);
        } catch (DataIntegrityViolationException e) {
            final Throwable dataIntegrityViolationCause = e.getCause();

            if (dataIntegrityViolationCause instanceof ConstraintViolationException) {
                final Throwable constraintViolationCause = dataIntegrityViolationCause.getCause();

                throw ExceptionsUtil.getRecipientGroupConstraintViolationException(
                        constraintViolationCause.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return savedRecipientGroupEntity;
    }

    private RecipientGroupEntity updateRecipientGroupEntityIfNecessary(
            RecipientGroupEntity recipientGroupEntity, RecipientGroupResource recipientGroupResource) {
        final String title = recipientGroupResource.getTitle();
        final String recipientIds = recipientGroupResource.getRecipientIds();

        if (title != null) {
            recipientGroupEntity.setTitle(title);
        }

        if (recipientIds != null) {
            recipientGroupEntity.setRecipientIds(recipientIds);
        }

        return saveRecipientGroupEntity(recipientGroupEntity);
    }

    private List<RecipientGroupResource> recipientGroupEntitiesToRecipientGroupResource(
            List<RecipientGroupEntity> recipientGroupEntities) {
        return recipientGroupEntities.stream()
                .map(RecipientGroupMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
