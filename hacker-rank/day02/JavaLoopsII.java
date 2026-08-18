/**
 * HackerRank Challenge: Java Loops II
 *
 * Challenge URL:
 * https://www.hackerrank.com/challenges/java-loops/problem
 *
 * Objective:
 * Practice nested loops, cumulative calculations, and powers of two.
 *
 * Problem Summary:
 * For each query containing a, b, and n, generate n terms of a series.
 *
 * Series:
 * term(i) = a + 2^0 * b + 2^1 * b + ... + 2^i * b
 *
 * Input Format:
 * The first integer is the number of queries.
 * Each query contains three integers: a, b, and n.
 *
 * Output Format:
 * Print the generated terms for each query on one line, separated by spaces.
 *
 * Approach:
 * Use an outer loop for the queries and an inner loop for the terms.
 * Maintain a cumulative sum and double the power-of-two multiplier after
 * each term.
 *
 * Time Complexity:
 * O(q * n)
 *
 * Space Complexity:
 * O(1), excluding the output.
 */

import java.util.Scanner;

public class JavaLoopsII {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int queryCount = scanner.nextInt();

        for (int query = 0; query < queryCount; query++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int n = scanner.nextInt();

            int sum = a;
            int powerOfTwo = 1;

            for(int term = 0; term < n; term++){
                sum = sum + powerOfTwo * b;
                if(term>0){
                    System.out.print(" "+sum);
                }else{
                    System.out.print(sum);
                }
                powerOfTwo = powerOfTwo*2;
            }
            System.out.println();
        }

        scanner.close();
    }
}