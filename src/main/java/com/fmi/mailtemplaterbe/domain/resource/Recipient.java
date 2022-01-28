package com.fmi.mailtemplaterbe.domain.resource;

import lombok.Data;

import java.util.Map;

/**
 * Model that is used for parsing the information about the recipients of an email, coming from {@link SendEmailResource}.
 */
@Data
public class Recipient {

    private String email;
    private Map<String, String> placeholders;
}
