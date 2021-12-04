package com.fmi.mailtemplaterbe.util;

import com.fmi.mailtemplaterbe.exception.EmailTemplateNotFoundException;
import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import org.springframework.http.HttpStatus;

public final class EmailTemplateExceptionsUtil {

    public static EmailTemplateNotFoundException getEmailTemplateNotFoundException(Long id) {
        return new EmailTemplateNotFoundException(
                ErrorDetails.builder()
                        .message("Email Template with id: " + id + " was not found.")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private EmailTemplateExceptionsUtil() {
    }
}
