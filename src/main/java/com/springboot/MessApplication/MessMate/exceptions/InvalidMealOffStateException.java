package com.springboot.MessApplication.MessMate.exceptions;

public class InvalidMealOffStateException extends RuntimeException {
    public InvalidMealOffStateException(String message) {
        super(message);
    }
}
