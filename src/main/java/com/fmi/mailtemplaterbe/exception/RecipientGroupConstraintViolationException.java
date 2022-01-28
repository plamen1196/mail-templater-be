package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class RecipientGroupConstraintViolationException extends RuntimeException {

    private static final long serialVersionUID = 1853208033367653515L;
    private ErrorDetails errorDetails;

    public RecipientGroupConstraintViolationException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
