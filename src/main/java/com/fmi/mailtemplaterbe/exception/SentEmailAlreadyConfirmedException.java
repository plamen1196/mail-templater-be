package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class SentEmailAlreadyConfirmedException extends RuntimeException {

    private static final long serialVersionUID = 3472255415460341784L;
    private ErrorDetails errorDetails;

    public SentEmailAlreadyConfirmedException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
