package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.domain.entity.EmailTemplateEntity;
import com.fmi.mailtemplaterbe.domain.resource.EmailTemplateResource;

import java.util.Arrays;
import java.util.List;

public final class EmailTemplateMapper {

    private EmailTemplateMapper() {

    }

    public static EmailTemplateResource entityToResource(EmailTemplateEntity emailTemplateEntity) {
        if (emailTemplateEntity == null) {
            return null;
        }

        return EmailTemplateResource.builder()
                .id(emailTemplateEntity.getId())
                .title(emailTemplateEntity.getTitle())
                .message(emailTemplateEntity.getMessage())
                .placeholders(parsePlaceholdersToMap(emailTemplateEntity.getPlaceholders()))
                .build();
    }

    private static List<String> parsePlaceholdersToMap(String placeholders) {
        if (placeholders == null || placeholders.isEmpty()) {
            // TODO: Improve error handling
            return null;
        }

        return Arrays.asList(placeholders.split(",", -1));
    }
}
