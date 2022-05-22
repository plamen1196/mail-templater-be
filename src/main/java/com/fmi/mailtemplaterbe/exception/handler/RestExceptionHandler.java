package com.fmi.mailtemplaterbe.exception.handler;

import com.fmi.mailtemplaterbe.exception.*;
import com.fmi.mailtemplaterbe.exception.error.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

/**
 * Handler of specific app-wide exceptions and mapping them into responses.
 * For full automatic mapping of all exceptions, we need to extend {@link ResponseEntityExceptionHandler}.
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { CustomBadRequestException.class })
    protected ResponseEntity<Object> handleCustomBadRequest(CustomBadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = { EmailTemplateNotFoundException.class })
    protected ResponseEntity<Object> handleEmailTemplateNotFound(EmailTemplateNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = { RecipientNotFoundException.class })
    protected ResponseEntity<Object> handleRecipientNotFound(RecipientNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {RecipientGroupNotFoundException.class })
    protected ResponseEntity<Object> handleRecipientGroupNotFound(RecipientGroupNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {SentEmailNotFoundException.class })
    protected ResponseEntity<Object> handleSentEmailNotFound(SentEmailNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {SentEmailAlreadyConfirmedException.class })
    protected ResponseEntity<Object> handleSentEmailAlreadyConfirmed(SentEmailAlreadyConfirmedException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {RecipientGroupConstraintViolationException.class })
    protected ResponseEntity<Object> handleRecipientGroupConstraintViolationException(RecipientGroupConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {EmailTemplateConstraintViolationException.class })
    protected ResponseEntity<Object> handleEmailTemplateConstraintViolationException(EmailTemplateConstraintViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = ex.getErrorDetails();

        return handleExceptionInternalCustom(ex, errorDetails, new HttpHeaders(), errorDetails.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalCustom(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
