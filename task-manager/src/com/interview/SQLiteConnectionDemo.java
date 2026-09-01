package com.interview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Verifies a minimal SQLite JDBC connection and task schema.
 */
public class SQLiteConnectionDemo {
    public static void main(String[] args)
            throws SQLException {
        SQLiteDatabase database =
                new SQLiteDatabase(
                        "jdbc:sqlite::memory:");

        try (Connection connection =
                database.openConnection()) {
            database.initializeSchema(connection);

            boolean foreignKeysEnabled =
                    readForeignKeyStatus(connection);
            boolean tasksTableExists =
                    checkTasksTable(connection);

            System.out.println(
                    "Connection open: "
                            + !connection.isClosed());
            System.out.println(
                    "Foreign keys enabled: "
                            + foreignKeysEnabled);
            System.out.println(
                    "Tasks table exists: "
                            + tasksTableExists);
        }
    }

    private static boolean readForeignKeyStatus(
            Connection connection) throws SQLException {
        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(
                            "PRAGMA foreign_keys")) {
            return resultSet.next()
                    && resultSet.getInt(1) == 1;
        }
    }

    private static boolean checkTasksTable(
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