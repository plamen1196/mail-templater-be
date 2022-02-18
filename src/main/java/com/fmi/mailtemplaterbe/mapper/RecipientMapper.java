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
                .firstName(recipientEntity.getFirstName())
                .lastName(recipientEntity.getLastName())
                .phoneNumber(recipientEntity.getPhoneNumber())
                .address(recipientEntity.getAddress())
                .build();
    }

    public static RecipientEntity resourceToEntity(RecipientResource recipientResource) {
        if (recipientResource == null) {
            return null;
        }

        return RecipientEntity.builder()
                .id(recipientResource.getId())
                .email(recipientResource.getEmail())
                .firstName(recipientResource.getFirstName())
                .lastName(recipientResource.getLastName())
                .phoneNumber(recipientResource.getPhoneNumber())
                .address(recipientResource.getAddress())
                .build();
    }
}
