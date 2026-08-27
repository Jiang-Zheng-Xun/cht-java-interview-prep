/*
 * HackerRank: Java Regex
 *
 * Problem:
 * Validate IPv4 address strings with a regular expression.
 *
 * Requirements:
 * - The address must contain exactly four dot-separated octets.
 * - Each octet must represent a value from 0 to 255.
 * - Each octet may contain at most three digits.
 * - Leading zeros are allowed.
 * - MyRegex must be non-public and contain a String pattern.
 * - Print true or false for each input line.
 *
 * Local execution:
 * - This file uses JavaRegex as the public class name.
 * - Rename JavaRegex to Solution in the HackerRank editor.
 */

import java.util.Scanner;

class MyRegex {
    private static final String OCTET =
            "(?:\\d{1,2}|[01]\\d{2}|2[0-4]\\d|25[0-5])";

    String pattern =
            "^" + OCTET + "(?:\\." + OCTET + "){3}$";
}

public class JavaRegex {
    public static void main(String[] args) {
        MyRegex myRegex = new MyRegex();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                System.out.println(input.matches(myRegex.pattern));
            }
        }
    }
}