package com.interview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskInputValidationDemo {
    private static final Pattern ADD_PATTERN =
            Pattern.compile("^ADD \\S(?:.*\\S)?$");

    private static final Pattern COMPLETE_PATTERN =
            Pattern.compile("^COMPLETE [1-9]\\d*$");

    private static final Pattern LIST_PATTERN =
            Pattern.compile("^LIST$");

    public static void main(String[] args) {
        String[] inputs = {
            "ADD Review regex",
            "ADD A",
            "ADD ",
            "ADD  Invalid title",
            "ADD Review regex ",
            "COMPLETE 2",
            "COMPLETE 0",
            "COMPLETE -1",
            "COMPLETE two",
            "LIST",
            "LIST all",
            "DELETE 2",
            "ADD\tReview regex",
            "ADD Review\tregex",
            "ADD Review regex\t",
            "add Review regex",
            "COMPLETE 02",
            "COMPLETE 10",
            "COMPLETE 2 ",
            "complete 2",
            "LIST ",
            "list"
        };

        for (String input : inputs) {
            System.out.printf(
                    "%-20s -> %s%n",
                    "\"" + displayInput(input) + "\"",
                    identifyCommand(input)
            );
        }
    }

    private static String identifyCommand(String input) {
        if (matches(ADD_PATTERN, input)) {
            return "Valid ADD";
        }

        if (matches(COMPLETE_PATTERN, input)) {
            return "Valid COMPLETE";
        }

        if (matches(LIST_PATTERN, input)) {
            return "Valid LIST";
        }

        return "Invalid command";
    }

    private static boolean matches(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private static String displayInput(String input) {
        return input
                .replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}