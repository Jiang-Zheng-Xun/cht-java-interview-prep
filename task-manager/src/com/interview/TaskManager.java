package com.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages Task objects and their state changes.
 */
public class TaskManager {
    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String title) {
        tasks.add(new Task(title));
    }

    public boolean completeTask(int taskNumber) {
        if (taskNumber < 1
                || taskNumber > tasks.size()) {
            return false;
        }

        Task task = tasks.get(taskNumber - 1);
        task.markCompleted();
        return true;
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
}