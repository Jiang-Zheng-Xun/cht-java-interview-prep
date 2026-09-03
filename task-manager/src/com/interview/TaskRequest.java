package com.interview;

/**
 * Represents one task received from an HTTP JSON request.
 *
 * @param title task title supplied by the client
 * @param completed requested completion state
 */
public record TaskRequest(
        String title,
        boolean completed) {

    /**
     * Maps this HTTP request DTO to the domain model.
     *
     * Domain validation remains in Task.
     *
     * @return a validated Task
     */
    public Task toTask() {
        Task task = new Task(title);

        if (completed) {
            task.markCompleted();
        }

        return task;
    }
}