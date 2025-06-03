package com.deloitte.elrr.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * @author mnelakurti
 */
@SuppressWarnings("checkstyle:linelength")
public class ELRRErrorDetailsTest {

    /**
     * @author phleven
     */
    @Test
    public void testTipExceptionWithMessage() {

        final String message = "Test exception";

        Exception ex = new Exception("Test exception");
        Date exDate = new Date();

        ELRRErrorDetails elrrErrorDetails = new ELRRErrorDetails(exDate, ex
                .getMessage(), ex.getMessage(), null);

        assertEquals(elrrErrorDetails.getMessage(), message);
        assertEquals(elrrErrorDetails.getPath(), "Test exception");
        assertEquals(elrrErrorDetails.getDetails(), null);
        assertEquals(elrrErrorDetails.getTimestamp(), exDate);

    }
}
