package com.springinventory.demo.exception;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),  request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> duplicationError( DataIntegrityViolationException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "My message",  request.getDescription(false));
        System.out.println("custom constraint violation");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        if(isCause(DataIntegrityViolationException.class, ex))
            System.out.println("----constraint violation:\n " + ((DataIntegrityViolationException)ex).getRootCause());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static boolean isCause(
            Class<? extends Throwable> expected,
            Throwable exc
    ) {
        return expected.isInstance(exc) || (
                exc != null && isCause(expected, exc.getCause())
        );
    }
}
