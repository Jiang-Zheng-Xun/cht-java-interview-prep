package com.interview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Opens SQLite connections and initializes the task schema.
 */
public class SQLiteDatabase {
    private static final String CREATE_TASKS_TABLE = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL
                    CHECK (trim(title) <> ''),
                completed INTEGER NOT NULL DEFAULT 0
                    CHECK (completed IN (0, 1))
            )
            """;

    private final String jdbcUrl;

    public SQLiteDatabase(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "JDBC URL cannot be blank.");
        }

        if (!jdbcUrl.startsWith("jdbc:sqlite:")) {
            throw new IllegalArgumentException(
                    "JDBC URL must use SQLite.");
        }

        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Opens a connection and enables SQLite foreign-key enforcement.
     *
     * @return an open connection owned by the caller
     * @throws SQLException if the connection cannot be established
     */
    public Connection openConnection() throws SQLException {
        Connection connection =
                DriverManager.getConnection(jdbcUrl);

        try (Statement statement =
                connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException exception) {
            connection.close();
            throw exception;
        }

        return connection;
    }

    /**
     * Creates the database schema on an existing connection.
     *
     * @param connection an open SQLite connection
     * @throws SQLException if schema initialization fails
     */
    public void initializeSchema(
            Connection connection) throws SQLException {
        Objects.requireNonNull(
                connection,
                "connection must not be null");

        try (Statement statement =
                connection.createStatement()) {
            statement.executeUpdate(CREATE_TASKS_TABLE);
        }
    }
}