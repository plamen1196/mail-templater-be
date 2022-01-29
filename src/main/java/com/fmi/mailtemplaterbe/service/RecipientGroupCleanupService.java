package com.fmi.mailtemplaterbe.service;

import com.fmi.mailtemplaterbe.domain.entity.RecipientGroupEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientGroupResource;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;
import com.fmi.mailtemplaterbe.mapper.RecipientGroupMapper;
import com.fmi.mailtemplaterbe.repository.RecipientGroupRepository;
import com.fmi.mailtemplaterbe.util.ExceptionsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipientGroupCleanupService {

    private final RecipientGroupRepository recipientGroupRepository;

    public List<RecipientGroupResource> removeRecipientFromAllRecipientGroups(Long recipientId) {
        List<RecipientGroupEntity> allRecipientGroupEntities = recipientGroupRepository.findAll();
        List<RecipientGroupEntity> affectedRecipientGroupEntities = new ArrayList<>();

        if (CollectionUtils.isEmpty(allRecipientGroupEntities)) {
            return Collections.emptyList();
        }

        for (RecipientGroupEntity recipientGroupEntity : allRecipientGroupEntities) {
            /* Remove the recipient from the list of recipient ids if present. */
            List<Long> recipientIds =
                    RecipientGroupMapper.parseRecipientIdsToList(recipientGroupEntity.getRecipientIds());

            if (recipientIds.contains(recipientId)) {
                recipientIds.remove(recipientId);

                /* Update and save the recipient group without the removed recipient id. */
                String newRecipientIds = RecipientGroupMapper.parseRecipientIdsToString(recipientIds);
                recipientGroupEntity.setRecipientIds(newRecipientIds);
                RecipientGroupEntity savedRecipientGroupEntity = recipientGroupRepository.save(recipientGroupEntity);
                affectedRecipientGroupEntities.add(savedRecipientGroupEntity);
            }
        }

        return recipientGroupEntitiesToRecipientGroupResource(affectedRecipientGroupEntities);
    }

    private List<RecipientGroupResource> recipientGroupEntitiesToRecipientGroupResource(
            List<RecipientGroupEntity> recipientGroupEntities) {
        return recipientGroupEntities.stream()
                .map(RecipientGroupMapper::entityToResource)
                .collect(Collectors.toList());
    }
}
