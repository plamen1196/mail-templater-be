package com.fmi.mailtemplaterbe.exception.handler;

import com.fmi.mailtemplaterbe.exception.*;
import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomBadRequestException.class })
    protected ResponseEntity<Object> handleCustomBadRequest(CustomBadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

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

    @ExceptionHandler(value = {RecipientGroupNotFoundException.class })
    protected ResponseEntity<Object> handleRecipientGroupNotFound(RecipientGroupNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {SentEmailNotFoundException.class })
    protected ResponseEntity<Object> handleSentEmailNotFound(SentEmailNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {RecipientGroupConstraintViolationException.class })
    protected ResponseEntity<Object> handleRecipientGroupConstraintViolationException(RecipientGroupConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {EmailTemplateConstraintViolationException.class })
    protected ResponseEntity<Object> handleEmailTemplateConstraintViolationException(EmailTemplateConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }
}
