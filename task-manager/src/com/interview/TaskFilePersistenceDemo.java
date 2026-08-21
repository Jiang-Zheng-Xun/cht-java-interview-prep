package com.interview;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.NoSuchFileException;

/**
 * Demonstrates saving and loading Task objects.
 */
public class TaskFilePersistenceDemo {
    private static final Path TASK_FILE =
            Path.of(
                    "task-manager",
                    "out",
                    "day05-tasks.txt");

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        TaskFileRepository repository =
                new TaskFileRepository();
        if (args.length == 2
            && args[0].equals("load")) {
            Path inputFile = Path.of(args[1]);

            try {
                List<Task> loadedTasks =
                        repository.loadTasks(inputFile);

                System.out.println(
                        "Loaded tasks: "
                                + loadedTasks.size());
                displayTasks(loadedTasks);
            } catch (NoSuchFileException exception) {
                System.out.println(
                        "Task file not found: "
                                + exception.getFile());
            } catch (IOException exception) {
                System.out.println(
                        "Task file error: "
                                + exception.getMessage());
            }

            return;
        }

        manager.addTask("Review exceptions");
        manager.addTask("Write file tests");
        manager.completeTask(2);

        try {
            repository.saveTasks(
                    manager.getTasks(),
                    TASK_FILE);

            List<Task> loadedTasks =
                    repository.loadTasks(TASK_FILE);

            System.out.println(
                    "Saved tasks: "
                            + manager.getTaskCount());
            System.out.println(
                    "Loaded tasks: "
                            + loadedTasks.size());

            displayTasks(loadedTasks);
        } catch (IOException exception) {
            System.out.println(
                    "Task file error: "
                            + exception.getMessage());
        }
    }

    private static void displayTasks(
            List<Task> tasks) {
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