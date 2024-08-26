package org.example.exception;

public class NoSheetException extends RuntimeException{
    public NoSheetException(String message) {
        super(message);
    }
}
