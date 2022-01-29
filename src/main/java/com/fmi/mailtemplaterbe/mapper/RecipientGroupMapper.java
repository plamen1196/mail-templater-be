package com.fmi.mailtemplaterbe.mapper;

import com.fmi.mailtemplaterbe.domain.entity.RecipientGroupEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientGroupResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class RecipientGroupMapper {

    private RecipientGroupMapper() {

    }

    public static RecipientGroupResource entityToResource(RecipientGroupEntity recipientGroupEntity) {
        if (recipientGroupEntity == null) {
            return null;
        }

        return RecipientGroupResource.builder()
                .id(recipientGroupEntity.getId())
                .title(recipientGroupEntity.getTitle())
                .recipientIds(recipientGroupEntity.getRecipientIds())
                .build();
    }

    public static RecipientGroupEntity resourceToEntity(RecipientGroupResource recipientGroupResource) {
        if (recipientGroupResource == null) {
            return null;
        }

        return RecipientGroupEntity.builder()
                .id(recipientGroupResource.getId())
                .title(recipientGroupResource.getTitle())
                .recipientIds(recipientGroupResource.getRecipientIds())
                .build();
    }

    public static List<Long> parseRecipientIdsToList(String recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            // TODO: Improve error handling
            return null;
        }

        return Arrays.asList(recipientIds.split(",", -1)).stream()
                .map(recipientId -> Long.valueOf(recipientId))
                .collect(Collectors.toList());
    }

    public static String parseRecipientIdsToString(List<Long> recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            return null;
        }

        List<String> recipientIdsAsStrings =
                recipientIds.stream().map(recipientId -> String.valueOf(recipientId)).collect(Collectors.toList());

        return String.join(",", recipientIdsAsStrings);
    }
}
