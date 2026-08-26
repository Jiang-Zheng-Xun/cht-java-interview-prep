package com.interview;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class TaskDeadlineDemo {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 8, 26);

        String[][] taskData = {
            {"Review Date API", "2026-08-25"},
            {"Write daily log", "2026-08-26"},
            {"Practice HackerRank", "2026-08-29"},
            {"Check invalid date", "2026-02-30"}
        };

        System.out.println("Today: "
                + today.format(DATE_FORMATTER));

        for (String[] data : taskData) {
            String title = data[0];
            String deadlineText = data[1];

            try {
                LocalDate deadline =
                        parseDeadline(deadlineText);

                String status =
                        classifyDeadline(today, deadline);

                System.out.println(
                        title + ": " + status);
            } catch (DateTimeParseException exception) {
                System.out.println(
                        title
                                + ": Invalid date: "
                                + deadlineText);
            }
        }
    }

    private static LocalDate parseDeadline(
            String deadlineText) {
        return LocalDate.parse(deadlineText, DATE_FORMATTER);
    }

    private static String classifyDeadline(
            LocalDate today,
            LocalDate deadline) {
        String result = "";
        long between;
        if(deadline.isBefore(today)){
            between = calculateDaysBetween(deadline, today);
            if(between == 1){
                result = "Overdue by 1 day";
            }else{
                result = "Overdue by " + between + " days";
            }
        }else if(deadline.isEqual(today)){
            result = "Due today";
        }else{
            between = calculateDaysBetween(today, deadline);
            if(between == 1){
                result = "Upcoming in 1 day";
            }else{
                result = "Upcoming in "+ between +" days";
            }
        }
        return result;
    }

    private static long calculateDaysBetween(
            LocalDate start,
            LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}