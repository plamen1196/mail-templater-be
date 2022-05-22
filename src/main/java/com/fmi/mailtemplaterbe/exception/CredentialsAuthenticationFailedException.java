package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class CredentialsAuthenticationFailedException extends RuntimeException {

    private static final long serialVersionUID = -6209597775688660041L;
    private ErrorDetails errorDetails;

    public CredentialsAuthenticationFailedException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
