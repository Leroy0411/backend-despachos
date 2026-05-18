package com.citt.exceptions;

public class DespachoNotFoundException extends RuntimeException {
    public DespachoNotFoundException(String message) {
        super(message);
    }
}
