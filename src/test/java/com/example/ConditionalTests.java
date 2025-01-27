package com.example;

import static org.junit.jupiter.api.condition.JRE.JAVA_10;
import static org.junit.jupiter.api.condition.JRE.JAVA_11;
import static org.junit.jupiter.api.condition.JRE.JAVA_8;
import static org.junit.jupiter.api.condition.JRE.JAVA_9;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;

class ConditionalTests {

    @Nested
    class OsTests {

        @Test
        @EnabledOnOs(MAC)
        void onlyOnMacOs() {
            // ...
        }

        @Test
        @EnabledOnOs({ LINUX, MAC })
        void onLinuxOrMac() {
            // ...
        }

        @Test
        @DisabledOnOs(WINDOWS)
        void notOnWindows() {
            // ...
        }

        @Test
        @DisabledOnOs(MAC)
        void notOnMac() {
            // ...
        }

        @Test
        @EnabledOnOs(architectures = "aarch64")
        void onAarch64() {
            // ...
        }

        @Test
        @EnabledOnOs(value = MAC, architectures = "aarch64")
        void onNewMacs() {
            // ...
        }
    }

    @Nested
    class JreTests {

        @Test
        @EnabledOnJre(JAVA_8)
        void onlyOnJava8() {
            // ...
        }

        @Test
        @EnabledOnJre({ JAVA_9, JAVA_10 })
        void onJava9Or10() {
            // ...
        }

        @Test
        @EnabledForJreRange(min = JAVA_9, max = JAVA_11)
        void fromJava9to11() {
            // ...
        }

        @Test
        @EnabledForJreRange(min = JAVA_9)
        void fromJava9toCurrentJavaFeatureNumber() {
            // ...
        }

        @Test
        @EnabledForJreRange(max = JAVA_11)
        void fromJava8To11() {
            // ...
        }

        @Test
        @DisabledOnJre(JAVA_9)
        void notOnJava9() {
            // ...
        }

        @Test
        @DisabledForJreRange(min = JAVA_9, max = JAVA_11)
        void notFromJava9to11() {
            // ...
        }

        @Test
        @DisabledForJreRange(min = JAVA_9)
        void notFromJava9toCurrentJavaFeatureNumber() {
            // ...
        }

        @Test
        @DisabledForJreRange(max = JAVA_11)
        void notFromJava8to11() {
            // ...
        }
    }

    @Nested
    class SystemPropertyTests {

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void onlyOn64BitArchitectures() {
            // ...
        }

        @Test
        @DisabledIfSystemProperty(named = "user.name", matches = "jg05cy")
        void notForUser() {
            // ...
        }
    }

    @Nested
    class EnvironmentVariableTests {

        @Test
        @EnabledIfEnvironmentVariable(named = "USER", matches = "jg05cy")
        void onlyOnStagingServer() {
            // ...
        }
    }

    @Nested
    class CustomConditionTests {

        @Test
        @EnabledIf("customCondition")  // new in JUnit 5.7
        void enabled() {
            // ...
        }

        @Test
        @DisabledIf("customCondition")
        void disabled() {
            // ...
        }

        // method takes no arguments and returns a boolean
        boolean customCondition() {
            return true;
        }
    }
}
