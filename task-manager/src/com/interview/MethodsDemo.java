package com.interview;

public class MethodsDemo {
    public static void main(String[] args) {
        String task = "Review ArrayList";

        printTask(task);

        String formattedTask = formatTask(task, 1);
        System.out.println(formattedTask);

        boolean valid = isValidTask(task);
        System.out.println("Valid task: " + valid);
    }

    public static void printTask(String task) {
        System.out.println("Task: " + task);
    }

    public static String formatTask(String task, int number) {
        String formatted = number + ". " + task;
        return formatted;
    }

    public static boolean isValidTask(String task) {
        return task != null && !task.trim().isEmpty();
    }
}