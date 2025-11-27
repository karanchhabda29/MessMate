package com.springboot.MessApplication.MessMate.advice;

import com.springboot.MessApplication.MessMate.exceptions.MealOffDeadlineException;
import com.springboot.MessApplication.MessMate.exceptions.ResourceNotFoundException;
import com.springboot.MessApplication.MessMate.exceptions.UserNotSubscribedException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(MealOffDeadlineException.class)
    public ResponseEntity<ApiResponse<?>> handleMealOffDeadlineException(MealOffDeadlineException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(UserNotSubscribedException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotSubscribedException(UserNotSubscribedException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return buildErrorResponseEntity(apiError);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatusCode());
    }
}
