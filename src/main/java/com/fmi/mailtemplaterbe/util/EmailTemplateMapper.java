package com.fmi.mailtemplaterbe.util;

import antlr.StringUtils;
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
                .placeholders(parsePlaceholdersToList(emailTemplateEntity.getPlaceholders()))
                .build();
    }

    public static EmailTemplateEntity resourceToEntity(EmailTemplateResource emailTemplateResource) {
        if (emailTemplateResource == null) {
            return null;
        }

        return EmailTemplateEntity.builder()
                .title(emailTemplateResource.getTitle())
                .message(emailTemplateResource.getMessage())
                .placeholders(parsePlaceholdersToString(emailTemplateResource.getPlaceholders()))
                .build();
    }

    private static List<String> parsePlaceholdersToList(String placeholders) {
        if (placeholders == null || placeholders.isEmpty()) {
            // TODO: Improve error handling
            return null;
        }

        return Arrays.asList(placeholders.split(",", -1));
    }

    private static String parsePlaceholdersToString(List<String> placeholders) {
        if (placeholders == null || placeholders.isEmpty()) {
            return null;
        }

        return String.join(",", placeholders);
    }
}
