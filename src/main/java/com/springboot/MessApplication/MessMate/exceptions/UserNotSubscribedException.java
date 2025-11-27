package com.springboot.MessApplication.MessMate.exceptions;

public class UserNotSubscribedException extends RuntimeException {
    public UserNotSubscribedException(String message) {
        super(message);
    }
}
