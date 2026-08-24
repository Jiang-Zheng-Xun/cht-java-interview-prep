package com.interview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskSortingDemo {
    public static void main(String[] args) {
        Task writeTests = new Task("Write tests");
        Task reviewCollections =
                new Task("Review collections");
        Task practiceSorting =
                new Task("Practice sorting");
        practiceSorting.markCompleted();

        List<Task> originalTasks = new ArrayList<>();
        originalTasks.add(writeTests);
        originalTasks.add(reviewCollections);
        originalTasks.add(practiceSorting);

        List<Task> tasksByTitle =
                new ArrayList<>(originalTasks);
        List<Task> tasksByStatusAndTitle =
                new ArrayList<>(originalTasks);

        Comparator<Task> byTitle =
            Comparator.comparing(Task::getTitle);

        tasksByTitle.sort(byTitle);

        Comparator<Task> byStatusAndTitle =
            Comparator.comparing(Task::isCompleted)
                .thenComparing(Task::getTitle);

        tasksByStatusAndTitle.sort(byStatusAndTitle);

        printTasks("Original order:", originalTasks);
        printTasks("Sorted by title:", tasksByTitle);
        printTasks(
                "Sorted by status and title:",
                tasksByStatusAndTitle);
    }

    private static void printTasks(
            String heading,
            List<Task> tasks) {
        System.out.println(heading);

        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);
            String marker =
                    task.isCompleted() ? "[x]" : "[ ]";

            System.out.println(
                    (index + 1)
                            + ". "
                            + marker
                            + " "
                            + task.getTitle());
        }
    }
}