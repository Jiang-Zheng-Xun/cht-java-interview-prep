/**
 * HackerRank Challenge: Java Loops I
 *
 * Challenge URL:
 * https://www.hackerrank.com/challenges/java-loops-i/problem
 *
 * Objective:
 * Practice using a for loop to perform repeated calculations and produce
 * formatted output.
 *
 * Problem Summary:
 * Read an integer and print its first ten multiples.
 *
 * Requirements:
 * 1. Read one integer from standard input.
 * 2. Use a loop with multipliers from 1 through 10.
 * 3. Print each multiplication result using the required format.
 *
 * Input Format:
 * A single integer is provided through standard input.
 *
 * Output Format:
 * Print ten lines in the following format:
 * number x multiplier = result
 *
 * Constraints:
 * - The input follows the range defined by the challenge.
 *
 * Example:
 * Input:
 * 2
 *
 * Output:
 * 2 x 1 = 2
 * 2 x 2 = 4
 * ...
 * 2 x 10 = 20
 *
 * Approach:
 * Use a for loop that starts at 1 and continues through 10. In each
 * iteration, multiply the input number by the loop counter and print
 * the formatted result.
 *
 * Time Complexity:
 * O(1)
 *
 * Space Complexity:
 * O(1)
 */

import java.util.Scanner;

public class JavaLoopsI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        for(int index=0; index < 10; index++){
            System.out.println(number+" x "+(index+1)+" = "+number*(index+1));
        }

        scanner.close();
    }
}
