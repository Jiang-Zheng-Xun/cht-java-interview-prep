/**
 * HackerRank Challenge: Java If-Else
 *
 * Challenge URL:
 * https://www.hackerrank.com/challenges/java-if-else/problem
 *
 * Objective:
 * Practice using conditional statements, comparison operators, logical
 * operators, and the modulo operator in Java.
 *
 * Problem Summary:
 * Read an integer and determine whether to print "Weird" or "Not Weird"
 * according to its parity and numeric range.
 *
 * Requirements:
 * 1. Print "Weird" if the number is odd.
 * 2. Print "Not Weird" if the number is even and between 2 and 5,
 *    inclusive.
 * 3. Print "Weird" if the number is even and between 6 and 20,
 *    inclusive.
 * 4. Print "Not Weird" if the number is even and greater than 20.
 *
 * Input Format:
 * A single integer is provided through standard input.
 *
 * Output Format:
 * Print either "Weird" or "Not Weird" according to the specified
 * conditions.
 *
 * Constraints:
 * - The input is a valid integer.
 * - The number follows the range defined by the challenge.
 *
 * Example:
 * Input:
 * 3
 *
 * Output:
 * Weird
 *
 * Approach:
 * First, use the modulo operator to determine whether the number is odd.
 * If it is even, use conditional statements to determine which numeric
 * range contains the number and print the corresponding result.
 *
 * Time Complexity:
 * O(1)
 *
 * Space Complexity:
 * O(1)
 */

import java.util.Scanner;

public class JavaIfElse {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        // TODO: Determine whether the output is "Weird" or "Not Weird".
        
        if(number>=1 && number<=100){
            if(number%2!=0){
                System.out.println("Weird");
            }
            else if(number<=5) {
                System.out.println("Not Weird");
            }
            else if(number<=20){
                System.out.println("Weird");
            }
            else{
                System.out.println("Not Weird");
            }
        }
        
        scanner.close();
    }
}
