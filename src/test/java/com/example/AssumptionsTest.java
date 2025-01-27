package com.example;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.EmptyStackException;
import java.util.Stack;
import org.junit.jupiter.api.Test;

class AssumptionsTest {

    private final Stack<String> stack = new Stack<>();

    @Test
    void testPopOnEmptyStack() {
        int initialSize = stack.size();

        // pre-condition check
        assumeTrue(initialSize > 0, "Stack should not be empty");

        String value = stack.pop();

        assertAll(
                () -> assertEquals(initialSize - 1, stack.size()),
                () -> assertEquals("value", value)
        );
    }

    @Test
    void testPopOnNotEmptyStack() {
        stack.push("value");

        int initialSize = stack.size();

        // pre-condition check
        assumeTrue(initialSize > 0, "Stack should not be empty");

        String value = stack.pop();

        assertAll(
                () -> assertEquals(initialSize - 1, stack.size()),
                () -> assertEquals("value", value)
        );
    }

    @Test
    void testPopAssumingThatItHappens() {
        // pre-condition check
        assumingThat(!stack.isEmpty(), stack::pop);

        assertAll(
                () -> assertEquals(0, stack.size()),
                () -> assertThrows(EmptyStackException.class, stack::pop)
        );
    }
}