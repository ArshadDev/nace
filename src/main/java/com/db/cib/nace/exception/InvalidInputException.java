package com.db.cib.nace.exception;


import java.io.IOException;

public class InvalidInputException extends IOException {
    public InvalidInputException(String message) {
        super(message);
    }
}
