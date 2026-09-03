package com.interview;

import java.util.Objects;

/**
 * Represents one task returned in an HTTP JSON response.
 *
 * @param title stored task title
 * @param completed stored completion state
 */
public record TaskResponse(
        String title,
        boolean completed) {

    /**
     * Maps a domain Task to an HTTP response DTO.
     *
     * @param task domain task
     * @return response representation
     */
    public static TaskResponse from(Task task) {
        Objects.requireNonNull(
                task,
                "task must not be null");

        return new TaskResponse(
                task.getTitle(),
                task.isCompleted());
    }
}