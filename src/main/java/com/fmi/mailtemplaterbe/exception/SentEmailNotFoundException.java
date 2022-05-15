package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class SentEmailNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4349449199041869266L;
    private ErrorDetails errorDetails;

    public SentEmailNotFoundException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
