package com.interview;

import static com.interview.TaskCommandValidator.CommandType.ADD;
import static com.interview.TaskCommandValidator.CommandType.COMPLETE;
import static com.interview.TaskCommandValidator.CommandType.INVALID;
import static com.interview.TaskCommandValidator.CommandType.LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TaskCommandValidatorJUnitTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("commandCases")
    void identifiesCommandType(
            String testName,
            String input,
            TaskCommandValidator.CommandType expected) {

        // Act
        TaskCommandValidator.CommandType actual =
                TaskCommandValidator.identify(input);

        // Assert
        assertEquals(expected, actual, testName);
    }

    private static Stream<Arguments> commandCases() {
        return Stream.of(
                Arguments.of("valid ADD", "ADD Review regex", ADD),
                Arguments.of("single-character ADD title", "ADD A", ADD),
                Arguments.of(
                        "ADD title with middle tab",
                        "ADD Review\tregex",
                        ADD
                ),
                Arguments.of("valid COMPLETE", "COMPLETE 2", COMPLETE),
                Arguments.of(
                        "multi-digit COMPLETE",
                        "COMPLETE 10",
                        COMPLETE
                ),
                Arguments.of("valid LIST", "LIST", LIST),
                Arguments.of("null input", (String) null, INVALID),
                Arguments.of("empty input", "", INVALID),
                Arguments.of("empty ADD title", "ADD ", INVALID),
                Arguments.of(
                        "leading ADD title whitespace",
                        "ADD  Invalid title",
                        INVALID
                ),
                Arguments.of(
                        "trailing ADD title whitespace",
                        "ADD Review regex ",
                        INVALID
                ),
                Arguments.of("zero COMPLETE ID", "COMPLETE 0", INVALID),
                Arguments.of(
                        "negative COMPLETE ID",
                        "COMPLETE -1",
                        INVALID
                ),
                Arguments.of(
                        "leading-zero COMPLETE ID",
                        "COMPLETE 02",
                        INVALID
                ),
                Arguments.of(
                        "LIST with extra content",
                        "LIST all",
                        INVALID
                ),
                Arguments.of(
                        "lowercase command",
                        "complete 2",
                        INVALID
                ),
                Arguments.of("unknown command", "DELETE 2", INVALID)
        );
    }
}