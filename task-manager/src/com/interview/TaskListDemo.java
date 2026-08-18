package com.interview;

import java.util.Scanner;

public class TaskListDemo {
    private static final int MIN_TASKS = 1;
    private static final int MAX_TASKS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many tasks? \n");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer.");
            scanner.close();
            return;
        }

        int taskCount = scanner.nextInt();

        scanner.nextLine();

        if (taskCount < MIN_TASKS || taskCount > MAX_TASKS) {
            System.out.printf(
                    "The number of tasks must be between %d and %d.%n",
                    MIN_TASKS,
                    MAX_TASKS);
            scanner.close();
            return;
        }

        String[] tasks = new String[taskCount];

        for (int index = 0; index < taskCount; index++) {
            while (true) {
                System.out.printf("Enter task %d: ", index + 1);

                String taskName = scanner.nextLine().trim();

                if(taskName.isEmpty()){
                    System.out.printf("Task name cannot be empty.\n");
                }
                else{
                    tasks[index] = taskName;
                    System.out.printf(taskName+"\n");
                    break;
                }
            }
        }

        System.out.println();
        System.out.println("Task List");

        for(int index=0; index < taskCount; index++){
            System.out.println((index+1)+". "+tasks[index]);
        }

        System.out.println();
        System.out.println("Total tasks: " + taskCount);

        scanner.close();
    }
}