package com.interview;

public class TaskDemo {
    public static void main(String[] args) {
        Task task = new Task("Review encapsulation");

        System.out.println("Title: " + task.getTitle());
        System.out.println(
                "Completed: " + task.isCompleted());

        task.markCompleted();

        System.out.println(
                "Completed after mark: "
                        + task.isCompleted());
    }
}