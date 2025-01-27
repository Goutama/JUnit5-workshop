package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AnnotatedElementContext;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.io.TempDirFactory;

class TempDirTest {
    @Test
    void writeItemsToFile(@TempDir(cleanup = CleanupMode.ALWAYS) Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "Hello, World!".getBytes());
        assertTrue(Files.exists(testFile));
    }

    @Test
    void copyFileFromSourceToTarget(@TempDir Path source, @TempDir Path target) throws IOException {
        Path sourceFile = source.resolve("test.txt");
        Files.write(sourceFile, List.of("a,b,c"));

        // Copying the file to the target directory
        Path targetFile = Files.copy(sourceFile, target.resolve("test.txt"));

        assertNotEquals(sourceFile, targetFile);
        assertEquals(List.of("a,b,c"), Files.readAllLines(targetFile));
    }

    @Test
    void factoryTest(@TempDir(factory = Factory.class) Path tempDir) {
        assertTrue(tempDir.getFileName().toString().startsWith("factoryTest"));
    }

    static class Factory implements TempDirFactory {

        @Override
        public Path createTempDirectory(AnnotatedElementContext elementContext, ExtensionContext extensionContext)
                throws IOException {
            return Files.createTempDirectory(extensionContext.getRequiredTestMethod().getName());
        }

    }

}