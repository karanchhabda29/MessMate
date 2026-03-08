package com.springboot.MessApplication.MessMate.advice;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String exception;
    private String message;
    private HttpStatusCode statusCode;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String exception,String message, HttpStatusCode statusCode) {
        this();
        this.exception = exception;
        this.message = message;
        this.statusCode = statusCode;
    }

}
