package org.example.exception;

public class NumberSheetsTooLarge extends RuntimeException {
    public NumberSheetsTooLarge(String message) {
        super(message);
    }
}
