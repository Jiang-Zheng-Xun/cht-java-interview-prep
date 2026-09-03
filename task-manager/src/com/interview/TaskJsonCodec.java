package com.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * Converts between task JSON and Java objects used at the HTTP boundary.
 */
public class TaskJsonCodec {
    private static final TypeReference<List<TaskRequest>>
            TASK_REQUEST_LIST_TYPE =
            new TypeReference<>() {
            };

    private final ObjectMapper objectMapper;

    public TaskJsonCodec() {
        this(new ObjectMapper());
    }

    TaskJsonCodec(ObjectMapper objectMapper) {
        this.objectMapper =
                Objects.requireNonNull(
                        objectMapper,
                        "objectMapper must not be null");
    }

    /**
     * Serializes domain tasks as a JSON response array.
     *
     * @param tasks tasks to serialize
     * @return JSON array
     * @throws JsonProcessingException if serialization fails
     */
    public String serializeTasks(List<Task> tasks)
            throws JsonProcessingException {
        Objects.requireNonNull(
                tasks,
                "tasks must not be null");

        List<TaskResponse> responses =
                tasks.stream()
                        .map(TaskResponse::from)
                        .toList();

        return objectMapper.writeValueAsString(
                responses);
    }

    /**
     * Deserializes a JSON request array and maps it to domain tasks.
     *
     * @param json request body
     * @return validated tasks
     * @throws JsonProcessingException if the JSON cannot be parsed
     */
    public List<Task> deserializeTasks(String json)
            throws JsonProcessingException {
        Objects.requireNonNull(
                json,
                "json must not be null");

        List<TaskRequest> requests =
                objectMapper.readValue(
                        json,
                        TASK_REQUEST_LIST_TYPE);

        if (requests == null) {
            throw new IllegalArgumentException(
                    "task request must be a JSON array");
        }

        return requests.stream()
                .map(TaskRequest::toTask)
                .toList();
    }
}