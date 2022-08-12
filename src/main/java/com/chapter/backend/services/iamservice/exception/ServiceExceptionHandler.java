package com.chapter.backend.services.iamservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<ErrorMessage> handleServiceException(ServiceException serviceException) {
        return ResponseEntity
                .status(serviceException.getHttpStatus())
                .body(buildErrorMessage(serviceException));
    }

    private ErrorMessage buildErrorMessage(ServiceException exception) {
        return new ErrorMessage.Builder()
                .withDescription(exception.getDescription())
                .withHttpStatus(exception.getHttpStatus())
                .build();
    }
}