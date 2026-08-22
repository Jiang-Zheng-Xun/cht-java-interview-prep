package com.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores Task objects in memory.
 *
 * The stored data exists only while this repository object is alive.
 */
public class InMemoryTaskRepository implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void saveTasks(List<Task> tasks) {
        Objects.requireNonNull(tasks, "tasks must not be null");

        List<Task> snapshot = List.copyOf(tasks);

        this.tasks.clear();
        this.tasks.addAll(snapshot);
    }

    @Override
    public List<Task> loadTasks() {
        return List.copyOf(tasks);
    }
}