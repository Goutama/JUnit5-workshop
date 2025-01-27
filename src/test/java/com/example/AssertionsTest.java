package com.example;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import org.example.Calculator;
import org.example.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AssertionsTest {

    private final Calculator calculator = new Calculator();
    private final Person person = new Person("Jane", "Doe");

    @Nested
    class StandardTests {

        @Test
        void standardAssertions() {
            assertEquals(4, calculator.multiply(2, 2),
                    "The optional failure message is now the last parameter");
        }

        @Test
        void testWithoutSupplier() {
            assertEquals(4,  // expected
                    calculator.multiply(2, 2), // method to verify
                    generateFailureMessage());  // generate failure method called even if no error
        }

        @Test
        void testWithSupplier() {
            assertEquals(4,   // expected
                    calculator.multiply(2, 2), // method to verify
                    () -> generateFailureMessage());  // generate failure message supplier NOT CALLED if no error
        }

        private String generateFailureMessage() {
            System.out.println("Generating failure message");
            return "Something went wrong";
        }
    }

    @Nested
    class GroupedTests {

        @Test
        void testGroupedAssertions() {
            // In a grouped assertion all assertions are executed, and all
            // failures will be reported together.
            // assertAll(String header, Executable...)
            assertAll("person",
                    () -> assertEquals("Jane", person.getFirstName()),
                    () -> assertEquals("Doe", person.getLastName())
            );
        }

        @Test
        void testDependentAssertions() {
            // Within a code block, if an assertion fails the
            // subsequent code in the same block will be skipped.
            assertAll("properties",
                    () -> {
                        String firstName = person.getFirstName();
                        assertNotNull(firstName);

                        // Executed only if the previous assertion is valid.
                        assertAll("first name",
                                () -> assertTrue(firstName.startsWith("J")),
                                () -> assertTrue(firstName.endsWith("e"))
                        );
                    },
                    () -> {
                        // Grouped assertion, so processed independently
                        // of results of first name assertions.
                        String lastName = person.getLastName();
                        assertNotNull(lastName);

                        // Executed only if the previous assertion is valid.
                        assertAll("last name",
                                () -> assertTrue(lastName.startsWith("D")),
                                () -> assertTrue(lastName.endsWith("e"))
                        );
                    }
            );
        }
    }

    @Nested
    class ExceptionTests {

        @Test
        void testExceptionIsThrown() {
            // The following assertion succeeds because the code under assertion
            // throws ArithmeticException which is a subclass of RuntimeException.
            Exception exception = assertThrows(RuntimeException.class, () ->
                    calculator.divide(1, 0));
            assertEquals("/ by zero", exception.getMessage());
        }

        @Test
        void testExceptionIsThrownWithExactMatch() {
            // The following assertion succeeds because the code under assertion throws
            // ArithmeticException which is exactly equal to the expected type.
            Exception exception = assertThrowsExactly(ArithmeticException.class, () ->
                    calculator.divide(1, 0));
            assertEquals("/ by zero", exception.getMessage());
        }

        @Test
        @DisplayName("IEEE 754 spec: Floating point / by zero results in INFINITY")
        void testExceptionIsNotThrown() {
            Double value = assertDoesNotThrow(() -> calculator.divide(1.0, 0.0));
            assertEquals(Double.POSITIVE_INFINITY, value);
        }
    }

    @Nested
    class TimeoutTests {

        @Test
        void timeoutNotExceeded() {
            // The following assertion succeeds, and returns the supplied object.
            String actualResult = assertTimeout(ofMinutes(2), () -> {
                return "a result";
            });
            assertEquals("a result", actualResult);
        }

        @Test
        void timeoutNotExceededWithMethod() {
            // The following assertion invokes a method reference and returns an object.
            String actualGreeting = assertTimeout(ofMinutes(2), TimeoutTests::greeting);
            assertEquals("Hello, World!", actualGreeting);
        }

        @Test
        void timeoutExceeded() {
            // The following assertion fails with an error message similar to:
            // execution exceeded timeout of 10 ms by 91 ms
            assertTimeout(ofMillis(10), () -> {
                // Simulate task that takes more than 10 ms.
                Thread.sleep(100);
            });
        }

        @Test
        void timeoutExceededWithPreemptiveTermination() {
            // The following assertion fails with an error message similar to:
            // execution timed out after 10 ms
            assertTimeoutPreemptively(ofMillis(10), () -> {
                // Simulate task that takes more than 10 ms.
                new CountDownLatch(1).await();
            });
        }

        private static String greeting() {
            return "Hello, World!";
        }
    }
}