package com.fmi.mailtemplaterbe.exception;

import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import lombok.Data;

@Data
public class CustomBadRequestException extends RuntimeException {

    private static final long serialVersionUID = -4703247345286515816L;
    private ErrorDetails errorDetails;

    public CustomBadRequestException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
