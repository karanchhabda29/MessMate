package com.springboot.MessApplication.MessMate.exceptions;

public class InvalidCustomOffRequestException extends RuntimeException {
    public InvalidCustomOffRequestException(String message) {
        super(message);
    }
}
