package com.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TaskServiceIntegrationTest {
    private static final String REJECT_TASK_TRIGGER = """
            CREATE TRIGGER reject_task
            BEFORE INSERT ON tasks
            WHEN NEW.title = 'Rejected task'
            BEGIN
                SELECT RAISE(ABORT, 'rejected task');
            END
            """;

    @TempDir
    Path tempDirectory;

    private SQLiteDatabase database;
    private SQLiteTaskRepository repository;
    private TaskService service;
    private Connection transactionConnection;
    private Connection observerConnection;

    @BeforeEach
    void setUp() throws SQLException {
        Path databasePath =
                tempDirectory.resolve(
                        "task-service.db");

        database =
                new SQLiteDatabase(
                        "jdbc:sqlite:" + databasePath);

        repository =
                new SQLiteTaskRepository();

        service =
                new TaskService(repository);

        transactionConnection =
                database.openConnection();

        database.initializeSchema(
                transactionConnection);

        observerConnection =
                database.openConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        SQLException closeFailure = null;

        if (observerConnection != null) {
            try {
                observerConnection.close();
            } catch (SQLException exception) {
                closeFailure = exception;
            }
        }

        if (transactionConnection != null) {
            try {
                transactionConnection.close();
            } catch (SQLException exception) {
                if (closeFailure == null) {
                    closeFailure = exception;
                } else {
                    closeFailure.addSuppressed(
                            exception);
                }
            }
        }

        if (closeFailure != null) {
            throw closeFailure;
        }
    }

    @Test
    void commitsAllReplacementTasks()
            throws SQLException {
        repository.insertTask(
                transactionConnection,
                new Task("Original task"));

        Task firstReplacement =
                new Task("Replacement task A");

        Task secondReplacement =
                new Task("Replacement task B");
        secondReplacement.markCompleted();

        service.replaceAllTasks(
                transactionConnection,
                List.of(
                        firstReplacement,
                        secondReplacement));

        List<Task> committedTasks =
                repository.findAll(
                        observerConnection);

        assertEquals(2, committedTasks.size());

        assertEquals(
                "Replacement task A",
                committedTasks.get(0).getTitle());

        assertFalse(
                committedTasks.get(0).isCompleted());

        assertEquals(
                "Replacement task B",
                committedTasks.get(1).getTitle());

        assertTrue(
                committedTasks.get(1).isCompleted());

        assertTrue(
                transactionConnection.getAutoCommit());

        assertFalse(
                transactionConnection.isClosed());
    }

    @Test
    void rollsBackAllChangesWhenReplacementFails()
            throws SQLException {
        repository.insertTask(
                transactionConnection,
                new Task("Original task"));

        createRejectTaskTrigger();

        SQLException exception =
                assertThrows(
                        SQLException.class,
                        () -> service.replaceAllTasks(
                                transactionConnection,
                                List.of(
                                        new Task(
                                                "Replacement task A"),
                                        new Task(
                                                "Rejected task"))));

        List<Task> tasksAfterRollback =
                repository.findAll(
                        observerConnection);

        assertTrue(
                exception.getMessage().contains(
                        "rejected task"));

        assertEquals(1, tasksAfterRollback.size());

        assertEquals(
                "Original task",
                tasksAfterRollback.get(0).getTitle());

        assertFalse(
                tasksAfterRollback.get(0).isCompleted());

        assertTrue(
                transactionConnection.getAutoCommit());

        assertFalse(
                transactionConnection.isClosed());
    }

    private void createRejectTaskTrigger()
            throws SQLException {
        try (Statement statement =
                transactionConnection.createStatement()) {
            statement.execute(
                    REJECT_TASK_TRIGGER);
        }
    }
}