package com.fmi.mailtemplaterbe.domain.resource;

import lombok.Data;

import java.util.Map;

/**
 * Model that is used for parsing the information about email confirmation.
 */
@Data
public class ConfirmationEmailResource {

    private String recipientEmail;
    private String recipientToken;
    private Long recipientConfirmation;
}
