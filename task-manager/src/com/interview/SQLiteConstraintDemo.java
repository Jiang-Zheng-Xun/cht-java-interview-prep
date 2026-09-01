package com.interview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Verifies Java validation, SQLite constraints,
 * and JDBC resource handling.
 */
public class SQLiteConstraintDemo {
    private static final String INSERT_RAW_TASK = """
            INSERT INTO tasks (title, completed)
            VALUES (?, ?)
            """;

    public static void main(String[] args)
            throws SQLException {
        boolean blankTaskRejected =
                isBlankTaskRejected();
        boolean invalidUrlRejected =
                isInvalidUrlRejected();

        SQLiteDatabase database =
                new SQLiteDatabase(
                        "jdbc:sqlite::memory:");
        SQLiteTaskRepository repository =
                new SQLiteTaskRepository();

        Connection connection =
                database.openConnection();

        try (connection) {
            database.initializeSchema(connection);

            repository.insertTask(
                    connection,
                    new Task("Valid task"));

            boolean blankTitleRejected =
                    isRawInsertRejected(
                            connection,
                            "   ",
                            0);
            boolean invalidCompletedRejected =
                    isRawInsertRejected(
                            connection,
                            "Invalid completed value",
                            2);

            System.out.println(
                    "Blank Task rejected by Java: "
                            + blankTaskRejected);
            System.out.println(
                    "Invalid JDBC URL rejected: "
                            + invalidUrlRejected);
            System.out.println(
                    "Blank title rejected by database: "
                            + blankTitleRejected);
            System.out.println(
                    "Invalid completed rejected by database: "
                            + invalidCompletedRejected);
            System.out.println(
                    "Valid task count: "
                            + countTasks(connection));
            System.out.println(
                    "Connection open during work: "
                            + !connection.isClosed());
        }

        System.out.println(
                "Connection closed after try: "
                        + connection.isClosed());
    }

    private static boolean isBlankTaskRejected() {
        try {
            new Task("   ");
            return false;
        } catch (IllegalArgumentException exception) {
            return true;
        }
    }

    private static boolean isInvalidUrlRejected() {
        try {
            new SQLiteDatabase(
                    "jdbc:postgresql://localhost/tasks");
            return false;
        } catch (IllegalArgumentException exception) {
            return true;
        }
    }

    private static boolean isRawInsertRejected(
            Connection connection,
            String title,
            int completed) throws SQLException {
        try (PreparedStatement statement =
                connection.prepareStatement(
                        INSERT_RAW_TASK)) {
            statement.setString(1, title);
            statement.setInt(2, completed);
            statement.executeUpdate();
            return false;
        } catch (SQLException exception) {
            return true;
        }
    }

    private static int countTasks(
            Connection connection) throws SQLException {
        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(
                            "SELECT COUNT(*) FROM tasks")) {
            if (!resultSet.next()) {
                throw new SQLException(
                        "Task count query returned no row.");
            }

            return resultSet.getInt(1);
        }
    }
}