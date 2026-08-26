/*
 * HackerRank: Java Date and Time
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-date-and-time/problem
 *
 * Problem:
 * Given a month, day, and year, determine the day of the week
 * for that date.
 *
 * Input format:
 * One line containing three space-separated integers:
 *
 * month day year
 *
 * Output format:
 * Print the day of the week in uppercase English.
 *
 * Official sample input:
 * 08 05 2015
 *
 * Official sample output:
 * WEDNESDAY
 *
 * Additional local test 1:
 * Input:
 * 02 29 2024
 *
 * Expected output:
 * THURSDAY
 *
 * Additional local test 2:
 * Input:
 * 01 01 2000
 *
 * Expected output:
 * SATURDAY
 *
 * Approach:
 * - Create a LocalDate from the supplied year, month, and day.
 * - Obtain its DayOfWeek value.
 * - Convert the result to an uppercase string.
 *
 * Complexity:
 * - Time: O(1)
 * - Extra space: O(1)
 *
 * Important notes:
 * - The input order is month, day, year.
 * - LocalDate.of() expects year, month, day.
 * - The challenge guarantees a valid date.
 * - LocalDate is immutable.
 *
 *  *
 * HackerRank editor skeleton:
 * - Provides class Result and the findDay() method signature.
 * - Provides class Solution, input parsing, and output handling.
 * - The requested implementation area is the body of
 *   Result.findDay(month, day, year).
 * - The local version remains a standalone executable class for
 *   repeatable compilation and testing.
 */

import java.time.LocalDate;
import java.util.Scanner;

public class JavaDateAndTime {
    public static String findDay(
            int month,
            int day,
            int year) {

        return LocalDate.of(year, month, day).getDayOfWeek().toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int month = scanner.nextInt();
        int day = scanner.nextInt();
        int year = scanner.nextInt();

        System.out.println(findDay(month, day, year));

        scanner.close();
    }
}