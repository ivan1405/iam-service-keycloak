package com.chapter.backend.services.iamservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorMessage {

    private HttpStatus httpStatus;
    private String description;

    static final class Builder {
        private HttpStatus httpStatus;
        private String description;

        public Builder withHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ErrorMessage build() {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setHttpStatus(httpStatus);
            errorMessage.setDescription(description);
            return errorMessage;
        }
    }
}