package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class EmailTemplateNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -42460500170776473L;
    private ErrorDetails errorDetails;

    public EmailTemplateNotFoundException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
