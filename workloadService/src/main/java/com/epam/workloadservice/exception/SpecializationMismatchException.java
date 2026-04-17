package com.epam.workloadservice.exception;

public class SpecializationMismatchException extends RuntimeException {
    public SpecializationMismatchException(String message) {
        super(message);
    }
}