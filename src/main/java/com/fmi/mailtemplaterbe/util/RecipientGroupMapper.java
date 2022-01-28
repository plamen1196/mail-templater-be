package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.domain.entity.RecipientGroupEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientGroupResource;

import java.util.Arrays;
import java.util.List;

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

    public static List<String> parseRecipientIdsToList(String recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            // TODO: Improve error handling
            return null;
        }

        return Arrays.asList(recipientIds.split(",", -1));
    }

    public static String parseRecipientIdsToString(List<String> recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            return null;
        }

        return String.join(",", recipientIds);
    }
}
