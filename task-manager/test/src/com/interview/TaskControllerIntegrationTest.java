package com.interview;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskControllerIntegrationTest {
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
    private TaskService taskService;
    private TaskJsonCodec jsonCodec;
    private HttpClient httpClient;
    private HttpServer server;
    private URI baseUri;

    @BeforeEach
    void setUp() throws Exception {
        Path databaseFile =
                tempDirectory.resolve(
                        "tasks.db");

        database =
                new SQLiteDatabase(
                        "jdbc:sqlite:"
                                + databaseFile);

        try (Connection connection =
                database.openConnection()) {
            database.initializeSchema(
                    connection);
        }

        taskService =
                new TaskService(
                        new SQLiteTaskRepository());

        jsonCodec =
                new TaskJsonCodec();

        httpClient =
                HttpClient.newHttpClient();

        startServer(database);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void getTasksReturnsJsonArray()
            throws Exception {
        Task storedTask =
                new Task("Stored task");
        storedTask.markCompleted();

        try (Connection connection =
                database.openConnection()) {
            taskService.replaceAllTasks(
                    connection,
                    List.of(storedTask));
        }

        HttpResponse<String> response =
                sendGet("/tasks");

        assertEquals(
                200,
                response.statusCode());

        assertTrue(
                response.headers()
                        .firstValue("Content-Type")
                        .orElseThrow()
                        .startsWith(
                                "application/json"));

        List<Task> tasks =
                jsonCodec.deserializeTasks(
                        response.body());

        assertEquals(1, tasks.size());
        assertEquals(
                "Stored task",
                tasks.get(0).getTitle());
        assertTrue(
                tasks.get(0).isCompleted());
    }

    @Test
    void putTasksReturnsNoContentAndPersistsData()
            throws Exception {
        String requestBody =
                """
                [
                  {
                    "title": "Review HTTP",
                    "completed": false
                  },
                  {
                    "title": "Practice REST",
                    "completed": true
                  }
                ]
                """;

        HttpResponse<String> putResponse =
                sendPut(
                        requestBody,
                        "application/json");

        assertEquals(
                204,
                putResponse.statusCode());
        assertTrue(
                putResponse.body().isEmpty());

        HttpResponse<String> getResponse =
                sendGet("/tasks");

        assertEquals(
                200,
                getResponse.statusCode());

        List<Task> storedTasks =
                jsonCodec.deserializeTasks(
                        getResponse.body());

        assertEquals(2, storedTasks.size());
        assertEquals(
                "Review HTTP",
                storedTasks.get(0).getTitle());
        assertFalse(
                storedTasks.get(0).isCompleted());
        assertEquals(
                "Practice REST",
                storedTasks.get(1).getTitle());
        assertTrue(
                storedTasks.get(1).isCompleted());
    }

    @Test
    void putTasksRollsBackWhenSecondInsertFails()
            throws Exception {
        Task originalTask =
                new Task("Original task");

        try (Connection connection =
                database.openConnection()) {
            taskService.replaceAllTasks(
                    connection,
                    List.of(originalTask));

            createRejectTaskTrigger(connection);
        }

        String requestBody =
                """
                [
                  {
                    "title": "Replacement task A",
                    "completed": false
                  },
                  {
                    "title": "Rejected task",
                    "completed": true
                  }
                ]
                """;

        HttpResponse<String> response =
                sendPut(
                        requestBody,
                        "application/json");

        assertEquals(
                500,
                response.statusCode());

        assertEquals(
                """
                {"error":"Internal server error"}
                """.strip(),
                response.body());

        assertFalse(
                response.body()
                        .contains("rejected task"));

        assertFalse(
                response.body()
                        .contains("SQLException"));

        try (Connection observerConnection =
                database.openConnection()) {
            List<Task> tasksAfterRollback =
                    taskService.findAllTasks(
                            observerConnection);

            assertEquals(
                    1,
                    tasksAfterRollback.size());

            assertEquals(
                    "Original task",
                    tasksAfterRollback.get(0)
                            .getTitle());

            assertFalse(
                    tasksAfterRollback.get(0)
                            .isCompleted());
        }
    }

    @Test
    void malformedJsonReturnsBadRequest()
            throws Exception {
        String malformedJson =
                """
                [
                  {"title": "Broken JSON"
                ]
                """;

        HttpResponse<String> response =
                sendPut(
                        malformedJson,
                        "application/json");

        assertEquals(
                400,
                response.statusCode());
        assertEquals(
                """
                {"error":"Invalid task request"}
                """.strip(),
                response.body());
    }

    @Test
    void blankTitleReturnsBadRequest()
            throws Exception {
        String requestBody =
                """
                [
                  {
                    "title": "   ",
                    "completed": false
                  }
                ]
                """;

        HttpResponse<String> response =
                sendPut(
                        requestBody,
                        "application/json");

        assertEquals(
                400,
                response.statusCode());
        assertEquals(
                """
                {"error":"Invalid task request"}
                """.strip(),
                response.body());
    }

    @Test
    void unsupportedMethodReturnsMethodNotAllowed()
            throws Exception {
        HttpRequest request =
                HttpRequest.newBuilder(
                                baseUri.resolve(
                                        "/tasks"))
                        .method(
                                "TRACE",
                                HttpRequest.BodyPublishers
                                        .noBody())
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString());

        assertEquals(
                405,
                response.statusCode());
        assertEquals(
                "GET, PUT",
                response.headers()
                        .firstValue("Allow")
                        .orElseThrow());
    }

    @Test
    void unknownPathReturnsNotFound()
            throws Exception {
        HttpResponse<String> response =
                sendGet("/unknown");

        assertEquals(
                404,
                response.statusCode());
        assertEquals(
                """
                {"error":"Resource not found"}
                """.strip(),
                response.body());
    }

    @Test
    void nonJsonContentTypeReturnsUnsupportedMediaType()
            throws Exception {
        HttpResponse<String> response =
                sendPut(
                        "plain text",
                        "text/plain");

        assertEquals(
                415,
                response.statusCode());
        assertEquals(
                """
                {"error":"Content-Type must be application/json"}
                """.strip(),
                response.body());
    }

    @Test
    void databaseFailureReturnsSafeInternalServerError()
            throws Exception {
        server.stop(0);
        server = null;

        SQLiteDatabase failingDatabase =
                new SQLiteDatabase(
                        "jdbc:sqlite::memory:") {
                    @Override
                    public Connection openConnection()
                            throws SQLException {
                        throw new SQLException(
                                "SELECT secret FROM tasks");
                    }
                };

        startServer(failingDatabase);

        HttpResponse<String> response =
                sendGet("/tasks");

        assertEquals(
                500,
                response.statusCode());
        assertEquals(
                """
                {"error":"Internal server error"}
                """.strip(),
                response.body());

        assertFalse(
                response.body()
                        .contains("SELECT"));
        assertFalse(
                response.body()
                        .contains("SQLException"));
    }

    private void createRejectTaskTrigger(
            Connection connection)
            throws SQLException {
        try (Statement statement =
                connection.createStatement()) {
            statement.execute(
                    REJECT_TASK_TRIGGER);
        }
    }

    private void startServer(
            SQLiteDatabase controllerDatabase)
            throws Exception {
        TaskController controller =
                new TaskController(
                        controllerDatabase,
                        taskService,
                        jsonCodec);

        server =
                HttpServer.create(
                        new InetSocketAddress(0),
                        0);

        server.createContext(
                "/",
                controller);

        server.start();

        baseUri =
                URI.create(
                        "http://localhost:"
                                + server.getAddress()
                                        .getPort());
    }

    private HttpResponse<String> sendGet(
            String path) throws Exception {
        HttpRequest request =
                HttpRequest.newBuilder(
                                baseUri.resolve(path))
                        .GET()
                        .build();

        return httpClient.send(
                request,
                HttpResponse.BodyHandlers
                        .ofString());
    }

    private HttpResponse<String> sendPut(
            String body,
            String contentType)
            throws Exception {
        HttpRequest request =
                HttpRequest.newBuilder(
                                baseUri.resolve(
                                        "/tasks"))
                        .header(
                                "Content-Type",
                                contentType)
                        .PUT(
                                HttpRequest.BodyPublishers
                                        .ofString(body))
                        .build();

        return httpClient.send(
                request,
                HttpResponse.BodyHandlers
                        .ofString());
    }
}