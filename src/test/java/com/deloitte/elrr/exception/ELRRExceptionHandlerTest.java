package com.deloitte.elrr.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
public class ELRRExceptionHandlerTest {

    @InjectMocks
    private ELRRExceptionHandler exHandler;

    @Mock
    private WebRequest webRequest;

    @Test
    public void testHandleGenericException() {

        Exception ex = new Exception("Test exception");

        ResponseEntity<Object> res = exHandler.handleGenericException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

    }

    @Test
    public void testHandleHttpRequestMethodNotSupported() {

        HttpRequestMethodNotSupportedException notSupportedEx = new HttpRequestMethodNotSupportedException("GET");

        ResponseEntity<Object> res = exHandler.handleHttpRequestMethodNotSupported(notSupportedEx, null,
                HttpStatus.METHOD_NOT_ALLOWED, webRequest);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, res.getStatusCode());

    }
}
