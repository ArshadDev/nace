package com.db.cib.nace.exception.handler;

import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.exception.NaceDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CentralisedExceptionHandler {

    @ExceptionHandler(NaceDataNotFoundException.class)
    public ResponseEntity<Object> handleNaceDataNotFoundException(NaceDataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidFileFormatException(InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the request due to internal server error ");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Failed to process request as the uploaded file size is exceeding the allowed size limit");
    }
}
