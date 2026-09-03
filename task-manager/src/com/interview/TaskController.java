package com.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Translates HTTP requests into task service calls and service results
 * into HTTP responses.
 */
public class TaskController implements HttpHandler {
    private static final String TASKS_PATH = "/tasks";
    private static final String JSON_CONTENT_TYPE =
            "application/json; charset=utf-8";

    private final SQLiteDatabase database;
    private final TaskService taskService;
    private final TaskJsonCodec jsonCodec;

    public TaskController(
            SQLiteDatabase database,
            TaskService taskService,
            TaskJsonCodec jsonCodec) {
        this.database =
                Objects.requireNonNull(
                        database,
                        "database must not be null");
        this.taskService =
                Objects.requireNonNull(
                        taskService,
                        "taskService must not be null");
        this.jsonCodec =
                Objects.requireNonNull(
                        jsonCodec,
                        "jsonCodec must not be null");
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {
        try {
            String path =
                    exchange.getRequestURI().getPath();

            if (!TASKS_PATH.equals(path)) {
                sendJson(
                        exchange,
                        404,
                        """
                        {"error":"Resource not found"}
                        """);
                return;
            }

            String method =
                    exchange.getRequestMethod();

            switch (method) {
                case "GET" -> handleGet(exchange);
                case "PUT" -> handlePut(exchange);
                default -> {
                    exchange.getResponseHeaders()
                            .set("Allow", "GET, PUT");

                    sendJson(
                            exchange,
                            405,
                            """
                            {"error":"Method not allowed"}
                            """);
                }
            }
        } catch (SQLException exception) {
            sendJson(
                    exchange,
                    500,
                    """
                    {"error":"Internal server error"}
                    """);
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange)
            throws IOException, SQLException {
        try (Connection connection =
                database.openConnection()) {
            List<Task> tasks =
                    taskService.findAllTasks(
                            connection);

            String responseBody;

            try {
                responseBody =
                        jsonCodec.serializeTasks(tasks);
            } catch (JsonProcessingException exception) {
                sendJson(
                        exchange,
                        500,
                        """
                        {"error":"Internal server error"}
                        """);
                return;
            }

            sendJson(
                    exchange,
                    200,
                    responseBody);
        }
    }

    private void handlePut(HttpExchange exchange)
            throws IOException, SQLException {
        if (!hasJsonContentType(exchange)) {
            sendJson(
                    exchange,
                    415,
                    """
                    {"error":"Content-Type must be application/json"}
                    """);
            return;
        }

        String requestBody =
                new String(
                        exchange.getRequestBody()
                                .readAllBytes(),
                        StandardCharsets.UTF_8);

        List<Task> replacementTasks;

        try {
            replacementTasks =
                    jsonCodec.deserializeTasks(
                            requestBody);
        } catch (JsonProcessingException
                | IllegalArgumentException exception) {
            sendJson(
                    exchange,
                    400,
                    """
                    {"error":"Invalid task request"}
                    """);
            return;
        }

        try (Connection connection =
                database.openConnection()) {
            taskService.replaceAllTasks(
                    connection,
                    replacementTasks);
        }

        exchange.sendResponseHeaders(
                204,
                -1);
    }

    private boolean hasJsonContentType(
            HttpExchange exchange) {
        String contentType =
                exchange.getRequestHeaders()
                        .getFirst("Content-Type");

        return contentType != null
                && contentType.toLowerCase(
                                Locale.ROOT)
                        .startsWith(
                                "application/json");
    }

    private void sendJson(
            HttpExchange exchange,
            int statusCode,
            String responseBody)
            throws IOException {
        byte[] responseBytes =
                responseBody.strip()
                        .getBytes(
                                StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        JSON_CONTENT_TYPE);

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length);

        try (OutputStream responseStream =
                exchange.getResponseBody()) {
            responseStream.write(
                    responseBytes);
        }
    }
}