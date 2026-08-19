/**
 * HackerRank: Java Strings Introduction
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-strings-introduction/problem
 *
 * Problem:
 * Given two non-empty lowercase English strings, first and second:
 *
 * 1. Print the sum of their lengths.
 * 2. Print "Yes" if first is lexicographically greater than second.
 *    Otherwise, print "No".
 * 3. Capitalize the first letter of both strings and print them on
 *    one line, separated by a space.
 *
 * Approach:
 * - Use String.length() to calculate the total length.
 * - Use String.compareTo() to compare the strings lexicographically.
 * - Check whether compareTo() returns a value greater than zero.
 * - Build new capitalized strings because String objects are immutable.
 *
 * Complexity:
 * Let A be the length of first and B be the length of second.
 *
 * - Calculating the lengths: O(1).
 * - Lexicographical comparison: O(min(A, B)) in the worst case.
 * - Creating the capitalized strings: O(A + B).
 * - Total time: O(A + B).
 * - Extra space: O(A + B) for the newly created strings.
 *
 * Important edge cases:
 * - Equal strings must produce "No".
 * - A one-character string has an empty substring after its first
 *   character.
 * - String comparison must use compareTo(), not ==, >, or <.
 * - Capitalization creates new strings; it does not modify the
 *   original String objects.
 *
 * Example input:
 * hello
 * java
 *
 * Example output:
 * 9
 * No
 * Hello Java
 */

import java.util.Scanner;

public class JavaStringsIntroduction {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String first = scanner.next();
        String second = scanner.next();

        System.out.println(first.length()+second.length());

        int result = first.compareTo(second);
        if(result > 0){
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }

        String First = first.substring(0, 1).toUpperCase()+first.substring(1);
        String Second = second.substring(0, 1).toUpperCase()+second.substring(1);
        System.out.println(First+" "+Second);

        scanner.close();
    }
}