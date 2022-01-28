package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.exception.EmailTemplateNotFoundException;
import com.fmi.mailtemplaterbe.exception.RecipientGroupConstraintViolationException;
import com.fmi.mailtemplaterbe.exception.RecipientGroupNotFoundException;
import com.fmi.mailtemplaterbe.exception.RecipientNotFoundException;
import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import org.springframework.http.HttpStatus;

public final class ExceptionsUtil {

    public static EmailTemplateNotFoundException getEmailTemplateNotFoundException(Long id) {
        return new EmailTemplateNotFoundException(
                ErrorDetails.builder()
                        .message("Email Template with id: " + id + " was not found.")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    public static RecipientNotFoundException getRecipientNotFoundException(Long id) {
        return new RecipientNotFoundException(
                ErrorDetails.builder()
                        .message("Recipient with id: " + id + " was not found.")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    public static RecipientGroupNotFoundException getRecipientGroupNotFoundException(Long id) {
        return new RecipientGroupNotFoundException(
                ErrorDetails.builder()
                        .message("Recipient group with id: " + id + " was not found.")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    public static RecipientGroupConstraintViolationException getRecipientGroupConstraintViolationException(
            String message) {
        return new RecipientGroupConstraintViolationException(
                ErrorDetails.builder()
                        .message("Constraint violation exception for recipient group: " + message)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private ExceptionsUtil() {
    }
}
