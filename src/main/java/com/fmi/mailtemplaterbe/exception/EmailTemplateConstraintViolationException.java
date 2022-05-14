package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class EmailTemplateConstraintViolationException extends RuntimeException {

    private static final long serialVersionUID = 3420774691231056073L;
    private ErrorDetails errorDetails;

    public EmailTemplateConstraintViolationException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
