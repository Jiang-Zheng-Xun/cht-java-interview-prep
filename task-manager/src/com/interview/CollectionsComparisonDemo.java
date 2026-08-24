package com.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsComparisonDemo {
    public static void main(String[] args) {
        List<String> taskList = new ArrayList<>();
        Set<String> taskSet = new HashSet<>();
        Map<String, Integer> taskPriorities = new HashMap<>();

        taskList.add("Review collections");
        taskList.add("Review collections");

        taskSet.add("Review collections");
        taskSet.add("Review collections");

        Integer firstPreviousPriority =
                taskPriorities.put("Review collections", 1);
        Integer secondPreviousPriority =
                taskPriorities.put("Review collections", 2);

        System.out.println("List size: " + taskList.size());
        System.out.println("List contents: " + taskList);

        System.out.println("Set size: " + taskSet.size());
        System.out.println(
                "Set contains task: "
                        + taskSet.contains("Review collections"));

        System.out.println("Map size: " + taskPriorities.size());
        System.out.println(
                "Current priority: "
                        + taskPriorities.get("Review collections"));

        System.out.println(
            "Map contains task: "
                    + taskPriorities.containsKey(
                            "Review collections"));

        System.out.println(
            "First previous priority: "
                + firstPreviousPriority);
        System.out.println(
            "Second previous priority: "
                + secondPreviousPriority);

    Task firstTask = new Task("Review collections");
    Task secondTask = new Task("Review collections");

    Set<Task> uniqueTasks = new HashSet<>();
    uniqueTasks.add(firstTask);
    uniqueTasks.add(secondTask);

    System.out.println(
            "Same Task reference: "
                    + (firstTask == secondTask));
    System.out.println(
            "Tasks equal: "
                    + firstTask.equals(secondTask));
    System.out.println(
            "Task Set size: "
                    + uniqueTasks.size());
    }
}