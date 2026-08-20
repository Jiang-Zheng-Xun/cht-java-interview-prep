package com.interview;

/**
 * Represents one task and its completion state.
 */
public class Task {
    private final String title;
    private boolean completed;

    public Task(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                "Task title cannot be blank.");
        }

        this.title = title.trim();
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }
}