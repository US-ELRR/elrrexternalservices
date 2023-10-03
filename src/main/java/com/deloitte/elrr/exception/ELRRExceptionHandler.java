/**
 *
 */
package com.deloitte.elrr.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ELRRExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            final Exception ex, final WebRequest request) {
        ELRRErrorDetails errorDetails = new ELRRErrorDetails(new Date(),
                ex.getMessage(), request.getDescription(true), null);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
