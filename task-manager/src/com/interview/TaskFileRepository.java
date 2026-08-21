package com.interview;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Saves and loads Task objects using a text file.
 *
 * File format:
 * completed<TAB>title
 *
 * Example:
 * false<TAB>Review exceptions
 * true<TAB>Write persistence tests
 */
public class TaskFileRepository {
    private static final String DELIMITER = "\t";

    /**
     * Saves all tasks to the specified file.
     *
     * @param tasks tasks to save
     * @param filePath destination file
     * @throws IOException if the file cannot be written
     */
    public void saveTasks(
            List<Task> tasks,
            Path filePath) throws IOException {
        Path parentDirectory =
        filePath.toAbsolutePath().getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (BufferedWriter writer =
            Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8)) {
            for (Task task : tasks) {
                String title = task.getTitle();

                if (title.contains(DELIMITER)
                    || title.contains("\n")
                    || title.contains("\r")) {
                    throw new IllegalArgumentException(
                        "Task title contains unsupported "
                            + "characters.");
                }

                writer.write(
                    Boolean.toString(
                        task.isCompleted()));
                writer.write(DELIMITER);
                writer.write(title);
                writer.newLine();
            }
        }
    }

    /**
     * Loads all tasks from the specified file.
     *
     * @param filePath source file
     * @return tasks reconstructed from the file
     * @throws IOException if the file cannot be read or its data
     *         is malformed
     */
    public List<Task> loadTasks(
            Path filePath) throws IOException {
        List<Task> tasks = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader =
            Files.newBufferedReader(
                    filePath,
                    StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                String[] parts =
                    line.split(DELIMITER, -1);

                if (parts.length != 2) {
                    throw new IOException(
                        "Malformed task data at line "
                                + lineNumber
                                + ".");
                }

                boolean completed;

                if (parts[0].equals("true")) {
                    completed = true;
                } else if (parts[0].equals("false")) {
                    completed = false;
                } else {
                    throw new IOException(
                        "Malformed task data at line "
                                + lineNumber
                                + ".");
                }

                Task task;

                try {
                    task = new Task(parts[1]);
                } catch (IllegalArgumentException exception) {
                    throw new IOException(
                        "Malformed task data at line "
                                + lineNumber
                                + ".",
                        exception);
                }

                if (completed) {
                    task.markCompleted();
                }

                tasks.add(task);
            }
        }

        return tasks;
    }
}