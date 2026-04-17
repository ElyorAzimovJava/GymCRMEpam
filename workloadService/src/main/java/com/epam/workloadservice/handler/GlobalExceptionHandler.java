package com.epam.workloadservice.handler;

import com.epam.workloadservice.exception.SpecializationMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SpecializationMismatchException.class)
    public ResponseEntity<String> handleSpecializationMismatchException(SpecializationMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}