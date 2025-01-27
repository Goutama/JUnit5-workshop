package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class ParallelTests {

    @Test
    void test1() {
        System.out.println("Test 1 - " + Thread.currentThread().getName());
    }

    @Test
    void test2() {
        System.out.println("Test 2 - " + Thread.currentThread().getName());
    }
}