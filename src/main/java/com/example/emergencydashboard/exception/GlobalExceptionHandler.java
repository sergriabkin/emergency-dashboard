package com.example.emergencydashboard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(TIMESTAMP, LocalDateTime.now());
        responseBody.put(STATUS, HttpStatus.BAD_REQUEST.value());
        responseBody.put(ERROR, "Validation Error");
        responseBody.put(MESSAGE, ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, this::getFieldValidationMessage)));

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    private String getFieldValidationMessage(FieldError fieldError) {
        return Optional.ofNullable(fieldError.getDefaultMessage())
                .orElse("message is not provided");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {

        log.info("Entity not found", ex);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(TIMESTAMP, LocalDateTime.now());
        responseBody.put(STATUS, HttpStatus.NOT_FOUND.value());
        responseBody.put(ERROR, "Entity Not Found");
        responseBody.put(MESSAGE, ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        log.error("Unexpected error", ex);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(TIMESTAMP, LocalDateTime.now());
        responseBody.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put(ERROR, "Internal Server Error");
        responseBody.put(MESSAGE, "An unexpected error occurred: " + ex.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
