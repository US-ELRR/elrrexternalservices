/**
 *
 */
package com.deloitte.elrr.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author mnelakurti
 *
 */
public class ResourceNotFoundExceptionTest {

    /**
     *
     */
    private final String message = "ResourceNotFoundException"
            + "Exception Message";
    /**
     *
     */
    private ResourceNotFoundException resourceNotFoundException =
            new ResourceNotFoundException(message);;

    /**
     *
     */

    @Test
    public void testTipExceptionWithMessage() {
      assertEquals(resourceNotFoundException.getMessage(), message);
     }
   }
