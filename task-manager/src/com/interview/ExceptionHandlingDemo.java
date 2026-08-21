package com.interview;

/**
 * Demonstrates validation with specific exception handling.
 */
public class ExceptionHandlingDemo {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(
                    "Usage: provide one task number.");
            return;
        }

        String input = args[0];

        try {
            int taskNumber = 0;
            taskNumber = Integer.parseInt(input);

            if(taskNumber < 1){
                throw new IllegalArgumentException("Task number must be positive.");
            }

            System.out.println(
                    "Valid task number: " + taskNumber);
        } catch (NumberFormatException exception) {
            System.out.println(
                    "Task number must be an integer.");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        } finally {
            System.out.println("Validation finished.");
        }
    }
}