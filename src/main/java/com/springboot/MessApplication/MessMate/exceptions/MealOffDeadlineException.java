package com.springboot.MessApplication.MessMate.exceptions;

public class MealOffDeadlineException extends RuntimeException {
    public MealOffDeadlineException(String message) {
        super(message);
    }
}
