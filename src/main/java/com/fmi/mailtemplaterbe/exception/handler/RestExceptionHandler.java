package com.fmi.mailtemplaterbe.exception.handler;

import com.fmi.mailtemplaterbe.exception.EmailTemplateNotFoundException;
import com.fmi.mailtemplaterbe.exception.RecipientNotFoundException;
import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EmailTemplateNotFoundException.class })
    protected ResponseEntity<Object> handleEmailTemplateNotFound(EmailTemplateNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = { RecipientNotFoundException.class })
    protected ResponseEntity<Object> handleRecipientNotFound(RecipientNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }
}
