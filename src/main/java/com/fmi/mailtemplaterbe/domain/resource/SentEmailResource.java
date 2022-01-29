package com.fmi.mailtemplaterbe.domain.resource;

import lombok.*;

@Data
@Builder
public class SentEmailResource {

    private Long id;
    private String subject;
    private String message;
    private String senderEmail;
    private String recipientEmail;
    private boolean sentSuccessfully;
}
