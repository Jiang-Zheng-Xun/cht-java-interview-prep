package com.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskJsonCodecTest {
    private final TaskJsonCodec codec =
            new TaskJsonCodec();

    @Test
    void serializesTasksAsJsonArray()
            throws Exception {
        Task first = new Task("Review JSON");
        Task second = new Task("Practice mapping");
        second.markCompleted();

        String json =
                codec.serializeTasks(
                        List.of(first, second));

        JsonNode root =
                new ObjectMapper().readTree(json);

        assertTrue(root.isArray());
        assertEquals(2, root.size());
        assertEquals(
                "Review JSON",
                root.get(0).get("title").asText());
        assertFalse(
                root.get(0).get("completed").asBoolean());
        assertEquals(
                "Practice mapping",
                root.get(1).get("title").asText());
        assertTrue(
                root.get(1).get("completed").asBoolean());
    }

    @Test
    void deserializesJsonArrayAsTasks()
            throws Exception {
        String json = """
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

        List<Task> tasks =
                codec.deserializeTasks(json);

        assertEquals(2, tasks.size());
        assertEquals(
                "Review HTTP",
                tasks.get(0).getTitle());
        assertFalse(tasks.get(0).isCompleted());
        assertEquals(
                "Practice REST",
                tasks.get(1).getTitle());
        assertTrue(tasks.get(1).isCompleted());
    }

    @Test
    void rejectsMalformedJson() {
        String malformedJson =
                """
                [
                  {"title": "Broken JSON"
                ]
                """;

        assertThrows(
                JsonProcessingException.class,
                () -> codec.deserializeTasks(
                        malformedJson));
    }

    @Test
    void rejectsBlankTaskTitle() {
        String json =
                """
                [
                  {
                    "title": "   ",
                    "completed": false
                  }
                ]
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> codec.deserializeTasks(json));
    }
}