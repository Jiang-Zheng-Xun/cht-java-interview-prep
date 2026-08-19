package com.interview;

import java.util.ArrayList;
import java.util.Scanner;

public class TaskManagerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> tasks = new ArrayList<>();

        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(scanner, tasks);
                    break;
                case "2":
                    displayTasks(tasks);
                    break;
                case "3":
                    searchTasks(scanner, tasks);
                    break;
                case "4":
                    deleteTask(scanner, tasks);
                    break;
                case "0":
                    running = false;
                    System.out.println("Task manager closed.");
                    break;
                default:
                    System.out.println("Invalid menu choice.");
            }
        }

        scanner.close();
    }

    public static void displayMenu() {
        System.out.println();
        System.out.println("Task Manager");
        System.out.println("1. Add task");
        System.out.println("2. Display tasks");
        System.out.println("3. Search tasks");
        System.out.println("4. Delete task");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    public static void addTask(
            Scanner scanner,
            ArrayList<String> tasks) {
        System.out.print("Enter task name: ");
        String task = scanner.nextLine().trim();

        if (task.isEmpty()) {
            System.out.println("Task name cannot be empty.");
            return;
        }

        tasks.add(task);
        System.out.println("Task added.");
    }

    public static void displayTasks(ArrayList<String> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        System.out.println("Task List:");

        for (int index = 0; index < tasks.size(); index++) {
            System.out.println(
                    (index + 1) + ". " + tasks.get(index));
        }
    }

    public static void searchTasks(
            Scanner scanner,
            ArrayList<String> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        boolean found = false;

        for (int index = 0; index < tasks.size(); index++) {
            String task = tasks.get(index);

            if (task.contains(keyword)) {
                System.out.println((index + 1) + ". " + task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching tasks found.");
        }
    }

    public static void deleteTask(
            Scanner scanner,
            ArrayList<String> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        displayTasks(tasks);
        System.out.print("Enter task number to delete: ");
        String input = scanner.nextLine().trim();

        try {
            int taskNumber = Integer.parseInt(input);

            if (taskNumber < 1 || taskNumber > tasks.size()) {
                System.out.println("Invalid task number.");
                return;
            }

            String removedTask = tasks.remove(taskNumber - 1);
            System.out.println("Deleted task: " + removedTask);
        } catch (NumberFormatException exception) {
            System.out.println("Task number must be an integer.");
        }
    }
}