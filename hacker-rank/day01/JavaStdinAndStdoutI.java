/**
 * HackerRank Challenge: Java Stdin and Stdout I
 *
 * Challenge URL:
 * https://www.hackerrank.com/challenges/java-stdin-and-stdout-1/problem
 *
 * Objective:
 * Practice reading integer input with Scanner and printing values to
 * standard output.
 *
 * Problem Summary:
 * Read three integers from standard input and print each integer on a
 * separate line in the same order.
 *
 * Requirements:
 * 1. Read three integer values.
 * 2. Store the values in integer variables.
 * 3. Print each value on a separate line.
 *
 * Input Format:
 * Three integers are provided through standard input, one per line.
 *
 * Output Format:
 * Print the three integers in their original order, one per line.
 *
 * Constraints:
 * - Each input value is a valid Java int.
 *
 * Example:
 * Input:
 * 42
 * 100
 * 125
 *
 * Output:
 * 42
 * 100
 * 125
 *
 * Approach:
 * Use Scanner.nextInt() three times, store the values, and print them
 * with System.out.println().
 *
 * Time Complexity:
 * O(1)
 *
 * Space Complexity:
 * O(1)
 */

import java.util.Scanner;

public class JavaStdinAndStdoutI {
    public static void main(String[] args){
         Scanner scanner = new Scanner(System.in);
         
        // TODO: Read the three Integers.
        int myInt = scanner.nextInt();
        int myInt2 = scanner.nextInt();
        int myInt3 = scanner.nextInt();
        // TODO: Print the three Integers.
        System.out.println(myInt);
        System.out.println(myInt2);
        System.out.println(myInt3);

        scanner.close();
    }    
}
