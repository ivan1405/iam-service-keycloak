package com.chapter.backend.services.iamservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final String description;
    private final HttpStatus httpStatus;

    public ServiceException(HttpStatus httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    public ServiceException(HttpStatus httpStatus, String description, Throwable inCause) {
        super(inCause.getMessage(), inCause);
        this.httpStatus = httpStatus;
        this.description = description;
    }
}