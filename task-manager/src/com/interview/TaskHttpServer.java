package com.interview;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Starts the lightweight Day 17 task HTTP API.
 */
public final class TaskHttpServer {
    private static final int PORT = 8080;

    private TaskHttpServer() {
    }

    public static void main(String[] args)
            throws IOException, SQLException {
        SQLiteDatabase database =
                new SQLiteDatabase(
                        "jdbc:sqlite:task-manager.db");

        try (Connection connection =
                database.openConnection()) {
            database.initializeSchema(connection);
        }

        SQLiteTaskRepository repository =
                new SQLiteTaskRepository();
        TaskService taskService =
                new TaskService(repository);
        TaskJsonCodec jsonCodec =
                new TaskJsonCodec();

        TaskController controller =
                new TaskController(
                        database,
                        taskService,
                        jsonCodec);

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0);

        /*
         * The root context lets TaskController return a controlled JSON
         * 404 response for paths other than /tasks.
         */
        server.createContext(
                "/",
                controller);

        server.setExecutor(null);

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> server.stop(0)));

        server.start();

        System.out.println(
                "Task API started at "
                        + "http://localhost:"
                        + PORT
                        + "/tasks");
    }
}