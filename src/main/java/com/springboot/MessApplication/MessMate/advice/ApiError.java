package com.springboot.MessApplication.MessMate.advice;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatusCode statusCode;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatusCode statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }

}
