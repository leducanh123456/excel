package org.example.exception;

public class SheetInvalidException extends RuntimeException {
    public SheetInvalidException(String message) {
        super(message);
    }
}
