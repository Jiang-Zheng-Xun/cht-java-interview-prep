package com.interview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists Task objects in SQLite.
 */
public class SQLiteTaskRepository {
    private static final String INSERT_TASK = """
            INSERT INTO tasks (title, completed)
            VALUES (?, ?)
            """;

    private static final String DELETE_ALL_TASKS = """
            DELETE FROM tasks
            """;

    private static final String SELECT_ALL_TASKS = """
            SELECT title, completed
            FROM tasks
            ORDER BY id
            """;




    /**
     * Inserts one task through an existing connection.
     *
     * The caller owns the connection and its transaction boundary.
     *
     * @param connection an open SQLite connection
     * @param task task to insert
     * @return the number of affected rows
     * @throws SQLException if the insert fails
     */
    public int insertTask(
            Connection connection,
            Task task) throws SQLException {
        Objects.requireNonNull(
                connection,
                "connection must not be null");
        Objects.requireNonNull(
                task,
                "task must not be null");

        try (PreparedStatement statement =
                connection.prepareStatement(INSERT_TASK)) {
            statement.setString(1, task.getTitle());
            statement.setInt(
                    2,
                    task.isCompleted() ? 1 : 0);

            return statement.executeUpdate();
        }
    }

    /**
     * Deletes every stored task through an existing connection.
     *
     * The caller owns the connection and its transaction boundary.
     * This method does not commit or roll back the transaction.
     *
     * @param connection an open SQLite connection
     * @return the number of deleted rows
     * @throws SQLException if the delete fails
     */
    public int deleteAll(
           Connection connection) throws SQLException {
        Objects.requireNonNull(
                connection,
                "connection must not be null");

        try (PreparedStatement statement =
                connection.prepareStatement(
                       DELETE_ALL_TASKS)) {
            return statement.executeUpdate();
        }
    }

    /**
    * Loads all tasks in insertion order.
    *
    * @param connection an open SQLite connection
    * @return an unmodifiable snapshot of stored tasks
    * @throws SQLException if the query fails
    */
    public List<Task> findAll(
            Connection connection) throws SQLException {
        Objects.requireNonNull(
            connection,
            "connection must not be null");

        List<Task> tasks = new ArrayList<>();

        try (PreparedStatement statement =
                    connection.prepareStatement(
                                SELECT_ALL_TASKS);
                ResultSet resultSet =
                    statement.executeQuery()) {
            while (resultSet.next()) {
                String title =
                        resultSet.getString("title");
                boolean completed =
                        resultSet.getInt("completed") == 1;

                Task task = new Task(title);

                if (completed) {
                    task.markCompleted();
                }

                tasks.add(task);
            }
        }

        return List.copyOf(tasks);
    }
}