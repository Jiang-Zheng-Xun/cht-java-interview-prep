package com.interview;

public class TaskInputValidationDemo {
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
            TaskCommandValidator.CommandType commandType =
                    TaskCommandValidator.identify(input);

            System.out.printf(
                    "%-22s -> %s%n",
                    "\"" + displayInput(input) + "\"",
                    formatResult(commandType)
            );
        }
    }

    private static String formatResult(
            TaskCommandValidator.CommandType commandType) {
        return switch (commandType) {
            case ADD -> "Valid ADD";
            case COMPLETE -> "Valid COMPLETE";
            case LIST -> "Valid LIST";
            case INVALID -> "Invalid command";
        };
    }

    private static String displayInput(String input) {
        return input
                .replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}