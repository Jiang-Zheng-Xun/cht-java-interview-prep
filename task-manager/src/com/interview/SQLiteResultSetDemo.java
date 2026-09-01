package com.interview;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Demonstrates ResultSet traversal and Task mapping.
 */
public class SQLiteResultSetDemo {
    public static void main(String[] args)
            throws SQLException {
        SQLiteDatabase database =
                new SQLiteDatabase(
                        "jdbc:sqlite::memory:");
        SQLiteTaskRepository repository =
                new SQLiteTaskRepository();

        try (Connection connection =
                database.openConnection()) {
            database.initializeSchema(connection);

            Task firstTask =
                    new Task("Review JDBC");

            Task secondTask =
                    new Task(
                            "Practice PreparedStatement");
            secondTask.markCompleted();

            Task thirdTask =
                    new Task("Review ResultSet");

            repository.insertTask(connection, firstTask);
            repository.insertTask(connection, secondTask);
            repository.insertTask(connection, thirdTask);

            List<Task> loadedTasks =
                    repository.findAll(connection);

            System.out.println(
                    "Loaded task count: "
                            + loadedTasks.size());

            printTasks(loadedTasks);
        }
    }

    private static void printTasks(
            List<Task> tasks) {
        for (int index = 0;
                index < tasks.size();
                index++) {
            Task task = tasks.get(index);
            String status =
                    task.isCompleted() ? "x" : " ";

            System.out.printf(
                    "%d. [%s] %s%n",
                    index + 1,
                    status,
                    task.getTitle());
        }
    }
}