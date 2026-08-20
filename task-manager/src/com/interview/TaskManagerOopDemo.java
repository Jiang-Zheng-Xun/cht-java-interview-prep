package com.interview;

import java.util.List;

public class TaskManagerOopDemo {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.addTask("Review OOP");
        manager.addTask("Write tests");

        System.out.println("Before completion:");
        displayTasks(manager.getTasks());

        boolean completed = manager.completeTask(2);
        System.out.println(
                "Complete task 2: " + completed);

        System.out.println("After completion:");
        displayTasks(manager.getTasks());

        boolean invalid = manager.completeTask(99);
        System.out.println(
                "Complete task 99: " + invalid);
    }

    public static void displayTasks(List<Task> tasks) {
        for (int index = 0;
                index < tasks.size();
                index++) {
            Task task = tasks.get(index);
            String status =
                    task.isCompleted() ? "[x]" : "[ ]";

            System.out.println(
                    (index + 1)
                            + ". "
                            + status
                            + " "
                            + task.getTitle());
        }
    }
}