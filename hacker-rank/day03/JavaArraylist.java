/**
 * HackerRank: Java Arraylist
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-arraylist/problem
 *
 * Problem:
 * Read multiple rows of integers. Each row may contain a different
 * number of elements, including zero elements.
 *
 * After storing all rows, process multiple queries. Each query contains
 * two one-based values, x and y, representing the row number and the
 * element number within that row.
 *
 * Print the requested integer when the position exists. Otherwise,
 * print "ERROR!".
 *
 * Approach:
 * - Use an ArrayList<ArrayList<Integer>> because each row may have a
 *   different length.
 * - Create one inner ArrayList for every input row, including empty rows.
 * - Convert each one-based query position to a zero-based index.
 * - Validate both the row and column bounds before calling get().
 *
 * Complexity:
 * Let R be the number of rows, E be the total number of stored integers,
 * and Q be the number of queries.
 *
 * - Building the nested lists: O(R + E) time.
 * - Processing all queries: O(Q) time because ArrayList.get() is O(1).
 * - Total time: O(R + E + Q).
 * - Space: O(R + E) for the nested lists and stored integers.
 *
 * Important edge cases:
 * - A row may contain zero elements but must still be added to the
 *   outer ArrayList.
 * - Query values are one-based, while ArrayList indexes are zero-based.
 * - Both lower and upper index bounds must be checked.
 */

import java.util.ArrayList;
import java.util.Scanner;

public class JavaArraylist {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rowCount = scanner.nextInt();

        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        for(int i = 0; i < rowCount; i++){
            int colCount = scanner.nextInt();
            ArrayList<Integer> row = new ArrayList<>();
            for(int j = 0; j < colCount; j++){
                int element = scanner.nextInt();
                row.add(element);
            }

            matrix.add(row);
        }

        int queryCount = scanner.nextInt();
        for(int query = 0; query < queryCount; query++){
            int x_position = scanner.nextInt();
            int y_position = scanner.nextInt();
            if(x_position >= 1
                && x_position <= matrix.size()
                && y_position >= 1
                && y_position <= matrix.get(x_position-1).size()){
                int result = matrix.get(x_position-1).get(y_position-1);
                System.out.println(result);
            }else{
                System.out.println("ERROR!");
            }
        }

        scanner.close();
    }
}