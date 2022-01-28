package com.fmi.mailtemplaterbe.domain.resource;

import lombok.Data;

import java.util.Map;

@Data
public class RecipientResource {

    private String email;
    private Map<String, String> placeholders;
}
