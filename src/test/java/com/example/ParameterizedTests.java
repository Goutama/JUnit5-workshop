package com.example;

import static org.apache.commons.lang3.StringUtils.isAllUpperCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.platform.commons.util.StringUtils.isBlank;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.math3.primes.Primes;
import org.example.Gender;
import org.example.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


class ParameterizedTests {

    @Nested
    @DisplayName("Value Source Tests")
    class ValueSourceTests {

        @ParameterizedTest(name = "Test {index}: {0} should be a prime number")
        @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29})
        @DisplayName("Test Prime Numbers")
        void testIsPrime(int arg) {
            assertTrue(Primes.isPrime(arg), () -> arg + " should be prime");
        }
    }

    @Nested
    @DisplayName("Enum Source Tests")
    class EnumSourceTests {

        @ParameterizedTest
        @EnumSource(ChronoUnit.class)
        @DisplayName("Test with all ChronoUnit values")
        void testWithEnumSource(TemporalUnit unit) {
            assertNotNull(unit);
        }

        @ParameterizedTest
        @EnumSource(names = {"DAYS", "HOURS"})
        @DisplayName("Test with specific ChronoUnit values")
        void testWithEnumSourceInclude(ChronoUnit unit) {
            assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.EXCLUDE, names = {"ERAS", "FOREVER"})
        @DisplayName("Test excluding specific ChronoUnit values")
        void testWithEnumSourceExclude(ChronoUnit unit) {
            assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.MATCH_ALL, names = "^.*DAYS$")
        @DisplayName("Test with ChronoUnit values matching regex")
        void testWithEnumSourceRegex(ChronoUnit unit) {
            assertTrue(unit.name().endsWith("DAYS"));
        }
    }

    @Nested
    @DisplayName("Method source Tests")
    class MethodSourceTests {

        @ParameterizedTest(name = "Test {index}: \"{0}\" contains only uppercase letters")
        @MethodSource("uppercaseStrings")
        @DisplayName("Test Uppercase Strings")
        void checkUppercaseStrings(String arg) {
            assertTrue(isAllUpperCase(arg), "\"" + arg + "\" should contain only uppercase letters");
        }

        @ParameterizedTest(name = "Test {index}: {0} is even should be {1}")
        @MethodSource("evenNumberProvider")
        @DisplayName("Test Even Numbers")
        void testIsEven(int number, boolean expected) {
            assertEquals(expected, isEven(number));
        }

        private static Stream<Arguments> evenNumberProvider() {
            return Stream.of(
                    Arguments.of(2, true),
                    Arguments.of(3, false),
                    Arguments.of(4, true),
                    Arguments.of(5, false),
                    Arguments.of(6, true)
            );
        }

        // Factory method for the @MethodSource
        private static Stream<String> uppercaseStrings() {
            return Stream.of("HELLO", "WORLD", "JAVA", "JUNIT");
        }

        private static boolean isEven(int number) {
            return number % 2 == 0;
        }
    }

    @Nested
    @DisplayName("Field Source Tests")
    class FieldSourceTests {

        static final List<String> listOfFruits = Arrays.asList("apple", "banana");

        static List<Arguments> stringIntAndListArguments = Arrays.asList(
                arguments("apple", 1, Arrays.asList("a", "b")),
                arguments("lemon", 2, Arrays.asList("x", "y"))
        );

        @ParameterizedTest(name = "Test {index}: Fruit is {0}")
        @FieldSource("listOfFruits")
        @DisplayName("Test single field source")
        void singleFieldSource(String fruit) {
            assertTrue(fruit != null && !fruit.isEmpty(), "Fruit should not be null or empty");
        }

        @ParameterizedTest
        @FieldSource("stringIntAndListArguments")
        @DisplayName("Test with multiple arguments from field source")
        void testWithMultiArgFieldSource(String str, int num, List<String> list) {
            assertEquals(5, str.length());
            assertTrue(num >= 1 && num <= 2);
            assertEquals(2, list.size());
        }
    }

    @Nested
    @DisplayName("CSV Source Tests")
    class CsvSourceTests {

        @ParameterizedTest
        @CsvSource({
                "apple,         1",
                "banana,        2",
                "strawberry,    3"
        })
        @DisplayName("Test with CSV source")
        void testWithCsvSource(String fruit, int rank) {
            assertNotNull(fruit);
            assertNotEquals(0, rank);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/data/two-column.csv", numLinesToSkip = 1)
        @DisplayName("Test with CSV file source from classpath")
        void testWithCsvFileSourceFromClasspath(String country, int reference) {
            assertNotNull(country);
            assertNotEquals(0, reference);
        }

        @ParameterizedTest(name = "[{index}] {arguments}")
        @CsvFileSource(resources = "/data/two-column.csv", useHeadersInDisplayName = true)
        @DisplayName("Test with CSV file source and headers")
        void testWithCsvFileSourceAndHeaders(String country, int reference) {
            assertNotNull(country);
            assertNotEquals(0, reference);
        }

        @ParameterizedTest
        @CsvSource({
                "Jane, Doe, F, 1990-05-20",
                "John, Doe, M, 1990-10-22"
        })
        void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
            Person person = new Person(arguments.getString(0),
                    arguments.getString(1),
                    arguments.get(2, Gender.class),
                    arguments.get(3, LocalDate.class));
           System.out.println(person);
           // perform assertions against person
        }

        @ParameterizedTest
        @CsvSource({
                "Jane, Doe, F, 1990-05-20",
                "John, Doe, M, 1990-10-22"
        })
        void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
            System.out.println(person);
            // perform assertions against person
        }

        @ParameterizedTest
        @CsvSource({
                "Jane, Doe, F, 1990-05-20",
                "John, Doe, M, 1990-10-22"
        })
        void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
            System.out.println(person);
            // perform assertions against person
        }

        public static class PersonAggregator implements ArgumentsAggregator {
            @Override
            public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
                return new Person(arguments.getString(0),
                        arguments.getString(1),
                        arguments.get(2, Gender.class),
                        arguments.get(3, LocalDate.class));
            }
        }
    }

    @Nested
    @DisplayName("Arguments Source Tests")
    class ArgumentsSourceTests {

        @ParameterizedTest
        @ArgumentsSource(MyArgumentsProvider.class)
        @DisplayName("Test with custom arguments source")
        void testWithArgumentsSource(String argument) {
            assertNotNull(argument);
        }

        static class MyArgumentsProvider implements ArgumentsProvider {

            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
                // Can be fetched from databases, files or external services
                return Stream.of("apple", "banana").map(Arguments::of);
            }
        }
    }

    @Nested
    @DisplayName("Null and Empty Source Tests")
    class NullAndEmptySourceTests {

        @ParameterizedTest(name = "Test {index}: input=\"{0}\" should be blank")
        @NullAndEmptySource
        @DisplayName("Test Blank Strings")
        void testIsBlank(String arg) {
            assertTrue(isBlank(arg), () -> arg + " should be blank");
        }
    }
}