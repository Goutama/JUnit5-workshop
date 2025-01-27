package com.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Tag("unit")
class UserServiceTest {
    @Test
    void testCreateUser() {
        // Unit test logic
    }
}

@Tag("integration")
class UserServiceIntegrationTest {
    @Test
    void testCreateUserInDatabase() {
        // Integration test logic
    }
}

@Suite
@SelectPackages("com.example")
@IncludeTags("unit")
class UnitTestSuite {
}

// mvn test -Dgroups="unit"