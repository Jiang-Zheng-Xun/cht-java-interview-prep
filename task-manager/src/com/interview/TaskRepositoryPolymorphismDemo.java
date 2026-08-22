package com.interview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates polymorphism through the TaskRepository interface.
 */
public class TaskRepositoryPolymorphismDemo {
    public static void main(String[] args) throws IOException {
        TaskRepository repository =
                new InMemoryTaskRepository();

        List<Task> sourceTasks = new ArrayList<>();

        Task firstTask = new Task("Review interfaces");
        Task secondTask = new Task("Practice polymorphism");
        secondTask.markCompleted();

        sourceTasks.add(firstTask);
        sourceTasks.add(secondTask);

        repository.saveTasks(sourceTasks);

        sourceTasks.clear();

        List<Task> loadedTasks = repository.loadTasks();

        System.out.println(
                "Repository type: "
                        + repository.getClass().getSimpleName());
        System.out.println(
                "Source tasks after clear: "
                        + sourceTasks.size());
        System.out.println(
                "Loaded tasks: "
                        + loadedTasks.size());

        printTasks(loadedTasks);

        try {
            loadedTasks.add(new Task("Unexpected task"));
        } catch (UnsupportedOperationException exception) {
            System.out.println("Loaded task list is unmodifiable.");
        }
    }

    private static void printTasks(List<Task> tasks) {
        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);

            String status = task.isCompleted() ? "x" : " ";

            System.out.printf(
                    "%d. [%s] %s%n",
                    index + 1,
                    status,
                    task.getTitle());
        }
    }
}