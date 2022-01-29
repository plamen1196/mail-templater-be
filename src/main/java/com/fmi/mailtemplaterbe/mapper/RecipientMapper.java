package com.fmi.mailtemplaterbe.mapper;

import com.fmi.mailtemplaterbe.domain.entity.RecipientEntity;
import com.fmi.mailtemplaterbe.domain.resource.RecipientResource;

public final class RecipientMapper {

    private RecipientMapper() {

    }

    public static RecipientResource entityToResource(RecipientEntity recipientEntity) {
        if (recipientEntity == null) {
            return null;
        }

        return RecipientResource.builder()
                .id(recipientEntity.getId())
                .email(recipientEntity.getEmail())
                .build();
    }

    public static RecipientEntity resourceToEntity(RecipientResource recipientResource) {
        if (recipientResource == null) {
            return null;
        }

        return RecipientEntity.builder()
                .id(recipientResource.getId())
                .email(recipientResource.getEmail())
                .build();
    }
}
