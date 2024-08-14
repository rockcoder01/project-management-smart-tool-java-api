package com.example.demo_project_management.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
    }

    public InvalidOperationException(String message, Exception e) {
        super(message, e);
    }
}
