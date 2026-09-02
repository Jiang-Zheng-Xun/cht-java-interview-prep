package com.interview;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates task-related business operations.
 *
 * The caller owns the Connection lifecycle. This service owns the
 * transaction boundary for the operations it coordinates.
 */
public class TaskService {
    private final SQLiteTaskRepository repository;

    public TaskService(
            SQLiteTaskRepository repository) {
        this.repository =
                Objects.requireNonNull(
                        repository,
                        "repository must not be null");
    }

    /**
     * Replaces all stored tasks as one transaction.
     *
     * Existing tasks are deleted and the replacement tasks are inserted
     * through the same Connection. All changes are committed together or
     * rolled back together.
     *
     * @param connection an open SQLite connection
     * @param replacementTasks tasks that replace the existing data
     * @throws SQLException if a database operation fails
     */
    public void replaceAllTasks(
            Connection connection,
            List<Task> replacementTasks)
            throws SQLException {
        Objects.requireNonNull(
                connection,
                "connection must not be null");
        Objects.requireNonNull(
                replacementTasks,
                "replacementTasks must not be null");

        List<Task> taskSnapshot =
                List.copyOf(replacementTasks);

        boolean originalAutoCommit =
                connection.getAutoCommit();

        boolean transactionStarted = false;
        Throwable primaryFailure = null;

        try {
            connection.setAutoCommit(false);
            transactionStarted = true;

            repository.deleteAll(connection);

            for (Task task : taskSnapshot) {
                repository.insertTask(
                        connection,
                        task);
            }

            connection.commit();
        } catch (SQLException | RuntimeException exception) {
            primaryFailure = exception;

            if (transactionStarted) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(
                            rollbackException);
                }
            }

            throw exception;
        } finally {
            try {
                connection.setAutoCommit(
                        originalAutoCommit);
            } catch (SQLException restoreException) {
                if (primaryFailure != null) {
                    primaryFailure.addSuppressed(
                            restoreException);
                } else {
                    throw restoreException;
                }
            }
        }
    }
}