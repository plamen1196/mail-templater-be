package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class RecipientNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4154818774607032914L;
    private ErrorDetails errorDetails;

    public RecipientNotFoundException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
