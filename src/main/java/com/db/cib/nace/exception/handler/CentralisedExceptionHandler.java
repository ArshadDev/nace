package com.db.cib.nace.exception.handler;

import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.exception.NaceDataNotFoundException;
import com.db.cib.nace.utils.CommonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CentralisedExceptionHandler {

    @ExceptionHandler(NaceDataNotFoundException.class)
    public ResponseEntity<Object> handleNaceDataNotFoundException(NaceDataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonUtil.buildResponse(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND.toString()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonUtil.buildResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonUtil.buildResponse("Failed to process the request due to internal server error ", HttpStatus.INTERNAL_SERVER_ERROR.toString()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(CommonUtil.buildResponse("Failed to process request as the uploaded file size is exceeding the allowed size limit", HttpStatus.PAYLOAD_TOO_LARGE.toString()));
    }
}
