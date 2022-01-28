package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class RecipientGroupNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -941434972051881901L;
    private ErrorDetails errorDetails;

    public RecipientGroupNotFoundException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
