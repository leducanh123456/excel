package org.example.exception;

public class ExcelNotValidException extends RuntimeException{
    public ExcelNotValidException(String msg) {
        super(msg);
    }
}
