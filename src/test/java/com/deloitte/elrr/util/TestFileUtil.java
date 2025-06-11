package com.deloitte.elrr.util;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

public final class TestFileUtil {

    /**
     * @param filename
     * @return file
     * @throws IOException
     */
    public static File getJsonTestFile(String filename) throws IOException {

        File file;

        try {
            file = new ClassPathResource(filename).getFile();
        } catch (IOException e) {
            throw e;
        }

        return file;
    }

    private TestFileUtil() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
}
