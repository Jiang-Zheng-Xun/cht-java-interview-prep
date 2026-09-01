package com.interview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstrates PreparedStatement parameter binding.
 */
public class SQLitePreparedStatementDemo {
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

            Task injectionShapedTask =
                    new Task(
                            "Review JDBC'); "
                            + "DROP TABLE tasks; --");

            int firstAffectedRows =
                    repository.insertTask(
                            connection,
                            firstTask);
            int secondAffectedRows =
                    repository.insertTask(
                            connection,
                            secondTask);
            int thirdAffectedRows =
                    repository.insertTask(
                            connection,
                            injectionShapedTask);

            System.out.println(
                    "First insert affected rows: "
                            + firstAffectedRows);
            System.out.println(
                    "Second insert affected rows: "
                            + secondAffectedRows);
            System.out.println(
                    "Third insert affected rows: "
                            + thirdAffectedRows);
            System.out.println(
                    "Stored task count: "
                            + countTasks(connection));
            System.out.println(
                    "Tasks table exists: "
                            + tasksTableExists(connection));
        }
    }

    private static int countTasks(
            Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks";

        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new SQLException(
                        "Task count query returned no row.");
            }

            return resultSet.getInt(1);
        }
    }

    private static boolean tasksTableExists(
            Connection connection) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM sqlite_master
                WHERE type = 'table'
                  AND name = 'tasks'
                """;

        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(sql)) {
            return resultSet.next()
                    && resultSet.getInt(1) == 1;
        }
    }
}