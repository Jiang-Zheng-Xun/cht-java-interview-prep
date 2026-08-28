package com.interview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TaskCommandValidator {
    private static final Pattern ADD_PATTERN =
            Pattern.compile("^ADD \\S(?:.*\\S)?$");

    private static final Pattern COMPLETE_PATTERN =
            Pattern.compile("^COMPLETE [1-9]\\d*$");

    private static final Pattern LIST_PATTERN =
            Pattern.compile("^LIST$");

    private TaskCommandValidator() {
    }

    public enum CommandType {
        ADD,
        COMPLETE,
        LIST,
        INVALID
    }

    public static CommandType identify(String input) {
        if (input == null) {
            return CommandType.INVALID;
        }

        if (matches(ADD_PATTERN, input)) {
            return CommandType.ADD;
        }

        if (matches(COMPLETE_PATTERN, input)) {
            return CommandType.COMPLETE;
        }

        if (matches(LIST_PATTERN, input)) {
            return CommandType.LIST;
        }

        return CommandType.INVALID;
    }

    private static boolean matches(
            Pattern pattern,
            String input) {
        Matcher matcher =
                pattern.matcher(input);

        return matcher.matches();
    }
}