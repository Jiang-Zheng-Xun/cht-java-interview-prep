package com.interview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskStreamDemo {
    public static void main(String[] args) {
        Task writeStreamDemo =
                new Task("Write stream demo");
        Task reviewLambdas =
                new Task("Review lambdas");
        Task practiceFiltering =
                new Task("Practice filtering");
        practiceFiltering.markCompleted();

        List<Task> tasks = new ArrayList<>();
        tasks.add(writeStreamDemo);
        tasks.add(reviewLambdas);
        tasks.add(practiceFiltering);

        printTasks("Source order before stream:", tasks);

        List<String> incompleteTitles =
            tasks.stream()
                    .filter(task -> !task.isCompleted())
                    .sorted(
                            Comparator.comparing(
                                    Task::getTitle))
                    .map(Task::getTitle)
                    .toList();

        System.out.println(
                "Incomplete titles: "
                        + incompleteTitles);

        printTasks("Source order after stream:", tasks);
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