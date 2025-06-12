package com.deloitte.elrr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class InputSanitizerTest {

    @Test
    public void testInputSanitizerPrivateConstructor() {

        try {
            // Get the constructor object for InputSanitizer and make it accessible
            Constructor<InputSanitizer> constructor = InputSanitizer.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (InvocationTargetException ie) {
            assertEquals("This is a utility class and cannot be instantiated", ie.getCause().getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

    }

}
