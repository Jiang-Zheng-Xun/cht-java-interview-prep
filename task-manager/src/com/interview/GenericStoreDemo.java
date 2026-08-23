package com.interview;

import java.util.List;

/**
 * Demonstrates type-safe reuse of GenericStore<T>.
 */
public class GenericStoreDemo {
    public static void main(String[] args) {
        GenericStore<Task> taskStore =
                new GenericStore<>();

        Task firstTask = new Task("Review generics");
        Task secondTask =
                new Task("Practice type safety");
        secondTask.markCompleted();

        taskStore.add(firstTask);
        taskStore.add(secondTask);

        Task loadedTask = taskStore.get(0);
        List<Task> taskSnapshot = taskStore.getAll();

        System.out.println(
                "Task store size: "
                        + taskStore.size());
        System.out.println(
                "First task: "
                        + loadedTask.getTitle());
        System.out.println(
                "Task snapshot size: "
                        + taskSnapshot.size());

        GenericStore<String> textStore =
                new GenericStore<>();

        textStore.add("Java");
        textStore.add("Generics");

        String loadedText = textStore.get(0);

        System.out.println(
                "Text store size: "
                        + textStore.size());
        System.out.println(
                "First text: "
                        + loadedText);
        System.out.println(
                "Text values: "
                        + textStore.getAll());

        try {
            taskSnapshot.add(
                    new Task("Unexpected task"));
        } catch (UnsupportedOperationException exception) {
            System.out.println(
                    "Task snapshot is unmodifiable.");
        }

        try {
            textStore.add(null);
        } catch (NullPointerException exception) {
            System.out.println(
                    "Null item rejected: "
                            + exception.getMessage());
        }
    }
}