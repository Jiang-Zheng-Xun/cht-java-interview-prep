/*
 * HackerRank: Java Interface
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-interface/problem
 *
 * Problem:
 * An AdvancedArithmetic interface declares:
 *
 * int divisor_sum(int n)
 *
 * Create a MyCalculator class that implements this interface.
 * divisor_sum(n) must return the sum of every positive divisor
 * of n.
 *
 * A divisor divides n without leaving a remainder.
 *
 * Input format:
 * One positive integer n.
 *
 * Constraints:
 * - n is a positive integer.
 * - n is at most 1000.
 *
 * Output format:
 * The provided program prints two lines:
 *
 * 1. The name of the implemented interface.
 * 2. The sum of all positive divisors of n.
 *
 * Official sample input:
 * 6
 *
 * Official sample output:
 * I implemented: AdvancedArithmetic
 * 12
 *
 * Official sample explanation:
 * The positive divisors of 6 are 1, 2, 3, and 6.
 * Their sum is 12.
 *
 * Additional local test 1:
 * Input:
 * 1
 *
 * Expected output:
 * I implemented: AdvancedArithmetic
 * 1
 *
 * Reason:
 * The only positive divisor of 1 is 1.
 *
 * Additional local test 2:
 * Input:
 * 1000
 *
 * Expected output:
 * I implemented: AdvancedArithmetic
 * 2340
 *
 * Reason:
 * This checks the maximum documented input boundary.
 *
 * Planned approach:
 * - Start the divisor sum at zero.
 * - Examine every integer from 1 through n.
 * - If n % candidate == 0, add candidate to the sum.
 * - Return the final sum.
 *
 * Complexity:
 * - Time: O(n), because every integer from 1 through n is
 *   checked once.
 * - Extra space: O(1), because only a fixed number of integer
 *   variables is required.
 *
 * Important edge cases:
 * - n = 1 has exactly one positive divisor.
 * - n may be prime, in which case its divisors are 1 and n.
 * - n itself must be included as a divisor.
 * - The upper boundary n = 1000 must remain valid.
 *
 * HackerRank editor note:
 * The Run Code skeleton used for this practice only provided
 * imports and an empty Solution.main(). Therefore, the
 * AdvancedArithmetic interface, MyCalculator implementation,
 * input handling, and output handling all had to be completed.
*/

import java.util.Scanner;

interface AdvancedArithmetic {
    int divisor_sum(int n);
}

class MyCalculator implements AdvancedArithmetic {
    @Override
    public int divisor_sum(int n) {
        int sum = 0;

        for(int candidate=1; candidate <= n; candidate++){
            if(n % candidate == 0){
                sum += candidate;
            }
        }
        return sum;
    }
}

public class JavaInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int number = scanner.nextInt();

        AdvancedArithmetic calculator = new MyCalculator();

        int divisorSum = calculator.divisor_sum(number);

        System.out.println(
                "I implemented: "
                        + calculator.getClass()
                                .getInterfaces()[0]
                                .getName());
        System.out.println(divisorSum);

        scanner.close();
    }
}