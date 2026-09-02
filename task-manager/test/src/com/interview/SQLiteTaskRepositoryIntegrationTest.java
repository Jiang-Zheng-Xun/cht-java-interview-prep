package com.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQLiteTaskRepositoryIntegrationTest {
    private static final String INSERT_RAW_TASK = """
            INSERT INTO tasks (title, completed)
            VALUES (?, ?)
            """;

    private SQLiteDatabase database;
    private SQLiteTaskRepository repository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        database =
                new SQLiteDatabase(
                        "jdbc:sqlite::memory:");
        repository =
                new SQLiteTaskRepository();
        connection =
                database.openConnection();

        database.initializeSchema(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void insertsAndLoadsTasksInIdOrder()
            throws SQLException {
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

        assertEquals(3, loadedTasks.size());

        assertEquals(
                "Review JDBC",
                loadedTasks.get(0).getTitle());
        assertEquals(
                false,
                loadedTasks.get(0).isCompleted());

        assertEquals(
                "Practice PreparedStatement",
                loadedTasks.get(1).getTitle());
        assertEquals(
                true,
                loadedTasks.get(1).isCompleted());

        assertEquals(
                "Review ResultSet",
                loadedTasks.get(2).getTitle());
        assertEquals(
                false,
                loadedTasks.get(2).isCompleted());
    }

    @Test
    void deletesAllTasksAndReturnsAffectedRowCount()
            throws SQLException {
        repository.insertTask(
                connection,
                new Task("Original task A"));

        repository.insertTask(
                connection,
                new Task("Original task B"));

        int deletedRows =
                repository.deleteAll(connection);

        List<Task> remainingTasks =
                repository.findAll(connection);

        assertEquals(2, deletedRows);
        assertTrue(remainingTasks.isEmpty());
    }

    @Test
    void storesInjectionShapedTitleAsData()
            throws SQLException {
        String title =
                "Review JDBC'); DROP TABLE tasks; --";

        repository.insertTask(
                connection,
                new Task(title));

        List<Task> loadedTasks =
                repository.findAll(connection);

        assertEquals(1, loadedTasks.size());
        assertEquals(
                title,
                loadedTasks.get(0).getTitle());
        assertEquals(1, countTasks());
    }

    @Test
    void rejectsBlankTitleThroughDatabaseConstraint() {
        assertThrows(
                SQLException.class,
                () -> insertRawTask("   ", 0));

        assertEquals(0, countTasksUnchecked());
    }

    @Test
    void rejectsInvalidCompletedValue() {
        assertThrows(
                SQLException.class,
                () -> insertRawTask(
                        "Invalid completed value",
                        2));

        assertEquals(0, countTasksUnchecked());
    }

    @Test
    void enablesForeignKeysForConnection()
            throws SQLException {
        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(
                            "PRAGMA foreign_keys")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }

    private void insertRawTask(
            String title,
            int completed) throws SQLException {
        try (PreparedStatement statement =
                connection.prepareStatement(
                        INSERT_RAW_TASK)) {
            statement.setString(1, title);
            statement.setInt(2, completed);
            statement.executeUpdate();
        }
    }

    private int countTasksUnchecked() {
        try {
            return countTasks();
        } catch (SQLException exception) {
            throw new AssertionError(
                    "Could not count tasks.",
                    exception);
        }
    }

    private int countTasks() throws SQLException {
        try (Statement statement =
                    connection.createStatement();
                ResultSet resultSet =
                    statement.executeQuery(
                            "SELECT COUNT(*) FROM tasks")) {
            assertTrue(resultSet.next());
            return resultSet.getInt(1);
        }
    }
}