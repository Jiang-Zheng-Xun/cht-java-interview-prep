package com.interview;

import java.io.IOException;
import java.util.List;

/**
 * Defines persistence operations for Task objects.
 *
 * Implementations decide where and how the tasks are stored.
 */
public interface TaskRepository {
    void saveTasks(List<Task> tasks) throws IOException;

    List<Task> loadTasks() throws IOException;
}