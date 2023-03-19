package com.ehizman.inventorymgt.controllerAdvice;

import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
@ToString
public class ApiError implements Serializable {

    private final HttpStatus status;
    private final String message;
    private List<String> errors;
    private String error;

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    //

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getError() {
        return error;
    }
}
