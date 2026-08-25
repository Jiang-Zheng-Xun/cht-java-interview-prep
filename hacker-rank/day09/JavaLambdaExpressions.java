/*
 * HackerRank: Java Lambda Expressions
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/
 * java-lambda-expressions/problem
 *
 * Problem:
 * Create three methods that each return a lambda expression:
 *
 * 1. isOdd() checks whether an integer is odd.
 * 2. isPrime() checks whether an integer is prime.
 * 3. isPalindrome() checks whether an integer reads the
 *    same forward and backward.
 *
 * Each input test case contains:
 *
 * - Operation 1: print ODD or EVEN.
 * - Operation 2: print PRIME or COMPOSITE.
 * - Operation 3: print PALINDROME or NOT PALINDROME.
 *
 * Approach:
 * - Define PerformOperation as a functional interface.
 * - Return one lambda expression from each MyMath method.
 * - Pass the selected lambda to MyMath.checker().
 *
 * Planned complexity:
 * Let N be the checked number and D be its digit count.
 *
 * - Odd check: O(1) time and O(1) extra space.
 * - Prime check: O(sqrt(N)) time and O(1) space.
 * - Palindrome check with a String representation:
 *   O(D) time and O(D) extra space.
 *
 * Important edge cases:
 * - 1 is not a prime number.
 * - 2 is the smallest prime number.
 * - Even numbers greater than 2 are not prime.
 * - A one-digit number is a palindrome.
 * - The prime loop only needs to test divisors through
 *   the square root of the number.
 *
 * Official sample input:
 * 5
 * 1 4
 * 2 5
 * 3 898
 * 1 3
 * 2 12
 *
 * Official sample output:
 * EVEN
 * PRIME
 * PALINDROME
 * ODD
 * COMPOSITE
 *
 * Additional local input:
 * 8
 * 1 1
 * 1 2
 * 2 1
 * 2 2
 * 2 9
 * 2 17
 * 3 121
 * 3 123
 *
 * Additional expected output:
 * ODD
 * EVEN
 * COMPOSITE
 * PRIME
 * COMPOSITE
 * PRIME
 * PALINDROME
 * NOT PALINDROME
 */

import java.util.Scanner;

@FunctionalInterface
interface PerformOperation {
    boolean check(int number);
}

class MyMath {
    static boolean checker(
            PerformOperation operation,
            int number) {
        return operation.check(number);
    }

    PerformOperation isOdd() {
        return number -> number % 2 != 0;
    }

    PerformOperation isPrime() {
        return number -> {
            if (number < 2) {
                return false;
            }

            for (int divisor = 2;
                    divisor <= number / divisor;
                    divisor++) {
                if (number % divisor == 0) {
                    return false;
                }
            }

            return true;
        };
    }

    PerformOperation isPalindrome() {
        return number -> {
            String text = Integer.toString(number);
            String reversed =
                    new StringBuilder(text)
                            .reverse()
                            .toString();

            return text.equals(reversed);
        };
    }
}

public class JavaLambdaExpressions {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyMath myMath = new MyMath();

        int testCount = scanner.nextInt();

        for (int test = 0; test < testCount; test++) {
            int operationType = scanner.nextInt();
            int number = scanner.nextInt();

            PerformOperation operation;
            String positiveResult;
            String negativeResult;

            if (operationType == 1) {
                operation = myMath.isOdd();
                positiveResult = "ODD";
                negativeResult = "EVEN";
            } else if (operationType == 2) {
                operation = myMath.isPrime();
                positiveResult = "PRIME";
                negativeResult = "COMPOSITE";
            } else {
                operation = myMath.isPalindrome();
                positiveResult = "PALINDROME";
                negativeResult = "NOT PALINDROME";
            }

            boolean result =
                    MyMath.checker(operation, number);

            System.out.println(
                    result
                            ? positiveResult
                            : negativeResult);
        }

        scanner.close();
    }
}