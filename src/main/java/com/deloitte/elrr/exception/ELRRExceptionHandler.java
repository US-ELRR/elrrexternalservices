/**
 *
 */
package com.deloitte.elrr.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ELRRExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @param ex
     * @param request
     * @return responseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(final Exception ex,
            final WebRequest request) {
        ELRRErrorDetails errorDetails = new ELRRErrorDetails(new Date(), ex
                .getMessage(), request.getDescription(true), null);
        log.error("Exception occurred: ");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        ELRRErrorDetails errorDetails = new ELRRErrorDetails(new Date(),
                "Not supported", ex.getMethod(), null);
        log.error(" Method not supported: " + ex.getMethod());
        return new ResponseEntity<>(errorDetails,
                HttpStatus.METHOD_NOT_ALLOWED);
    }
}
