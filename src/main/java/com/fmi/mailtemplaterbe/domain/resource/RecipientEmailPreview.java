package com.fmi.mailtemplaterbe.domain.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipientEmailPreview {

    private String email;
    private String subject;
    private String message;
}
