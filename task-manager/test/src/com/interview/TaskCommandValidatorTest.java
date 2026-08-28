package com.interview;

public class TaskCommandValidatorTest {
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        testValidCommands();
        testInvalidCommands();

        System.out.printf(
                "Summary: %d passed, %d failed%n",
                passed,
                failed
        );

        if (failed > 0) {
            throw new AssertionError(
                    failed + " test(s) failed"
            );
        }
    }

    private static void testValidCommands() {
        assertCommandType(
                "valid ADD",
                "ADD Review regex",
                TaskCommandValidator.CommandType.ADD
        );

        assertCommandType(
                "single-character ADD title",
                "ADD A",
                TaskCommandValidator.CommandType.ADD
        );

        assertCommandType(
                "ADD title with middle tab",
                "ADD Review\tregex",
                TaskCommandValidator.CommandType.ADD
        );

        assertCommandType(
                "valid COMPLETE",
                "COMPLETE 2",
                TaskCommandValidator.CommandType.COMPLETE
        );

        assertCommandType(
                "multi-digit COMPLETE",
                "COMPLETE 10",
                TaskCommandValidator.CommandType.COMPLETE
        );

        assertCommandType(
                "valid LIST",
                "LIST",
                TaskCommandValidator.CommandType.LIST
        );
    }

    private static void testInvalidCommands() {
        assertCommandType(
                "null input",
                null,
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "empty input",
                "",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "empty ADD title",
                "ADD ",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "leading ADD title whitespace",
                "ADD  Invalid title",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "trailing ADD title whitespace",
                "ADD Review regex ",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "zero COMPLETE ID",
                "COMPLETE 0",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "negative COMPLETE ID",
                "COMPLETE -1",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "leading-zero COMPLETE ID",
                "COMPLETE 02",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "LIST with extra content",
                "LIST all",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "lowercase command",
                "list",
                TaskCommandValidator.CommandType.INVALID
        );

        assertCommandType(
                "unknown command",
                "DELETE 2",
                TaskCommandValidator.CommandType.INVALID
        );
    }

    private static void assertCommandType(
            String testName,
            String input,
            TaskCommandValidator.CommandType expected) {
        // Arrange: testName, input, and expected are provided.

        // Act
        TaskCommandValidator.CommandType actual =
                TaskCommandValidator.identify(input);

        // Assert
        if (actual == expected) {
            passed++;
            System.out.println("PASS: " + testName);
            return;
        }

        failed++;
        System.err.printf(
                "FAIL: %s | input=%s | expected=%s | actual=%s%n",
                testName,
                displayInput(input),
                expected,
                actual
        );
    }

    private static String displayInput(String input) {
        if (input == null) {
            return "<null>";
        }

        return "\""
                + input
                        .replace("\t", "\\t")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                + "\"";
    }
}