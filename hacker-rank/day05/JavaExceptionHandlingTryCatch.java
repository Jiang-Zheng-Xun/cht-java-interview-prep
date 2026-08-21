/*
 * HackerRank: Java Exception Handling (Try-catch)
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-exception-handling-try-catch/problem
 *
 * Problem:
 * Read two input values as 32-bit signed integers.
 *
 * If both values are valid integers, print the result of integer
 * division:
 *
 * first / second
 *
 * If either value cannot be read as an int, print the resulting
 * InputMismatchException.
 *
 * If the second integer is zero, print the resulting
 * ArithmeticException.
 *
 * Input:
 * Two input values:
 *
 * 1. first  - the dividend
 * 2. second - the divisor
 *
 * Both values must be valid Java int values in the range:
 *
 * -2147483648 to 2147483647
 *
 * Output:
 * Print exactly one of the following:
 *
 * 1. The integer quotient when both inputs are valid and the
 *    divisor is not zero.
 * 2. java.util.InputMismatchException when Scanner.nextInt()
 *    cannot read either value as an int.
 * 3. java.lang.ArithmeticException: / by zero when the divisor
 *    is zero.
 *
 * Approach:
 * - Create a Scanner for standard input.
 * - Read both values with Scanner.nextInt() inside a try block.
 * - Divide first by second using Java integer division.
 * - Catch InputMismatchException for non-integer, decimal, or
 *   out-of-int-range input.
 * - Print only the fully qualified InputMismatchException class
 *   name so the output does not depend on runtime-specific
 *   exception messages.
 * - Catch ArithmeticException for division by zero.
 * - Print the ArithmeticException object because the required
 *   output includes the "/ by zero" message.
 * - Close the Scanner after processing.
 *
 * Complexity:
 * The program reads and processes a fixed number of values.
 *
 * - Time: O(1).
 * - Extra space: O(1).
 *
 * Important edge cases:
 * - Valid positive integers, such as 10 and 3.
 * - Integer division discards the fractional part: 10 / 3 is 3.
 * - A negative dividend produces a negative quotient.
 * - A negative divisor produces a negative quotient.
 * - Two negative values produce a positive quotient.
 * - A zero dividend is valid when the divisor is not zero.
 * - A zero divisor throws ArithmeticException.
 * - A non-integer first value throws InputMismatchException.
 * - A non-integer second value throws InputMismatchException.
 * - A decimal value is not accepted by Scanner.nextInt().
 * - A value outside the Java int range throws
 *   InputMismatchException.
 * - Leading and trailing whitespace are accepted by Scanner.
 *
 * Official sample input 0:
 * 10
 * 3
 *
 * Official sample output 0:
 * 3
 *
 * Official sample input 1:
 * 10
 * Hello
 *
 * Official sample output 1:
 * java.util.InputMismatchException
 *
 * Official sample input 2:
 * 10
 * 0
 *
 * Official sample output 2:
 * java.lang.ArithmeticException: / by zero
 *
 * Official sample input 3:
 * 23.323
 * 0
 *
 * Official sample output 3:
 * java.util.InputMismatchException
 *
 * Additional local test cases:
 *
 * Case 1 - negative dividend:
 * Input:
 * -10
 * 3
 * Expected output:
 * -3
 *
 * Case 2 - negative divisor:
 * Input:
 * 10
 * -3
 * Expected output:
 * -3
 *
 * Case 3 - two negative integers:
 * Input:
 * -10
 * -2
 * Expected output:
 * 5
 *
 * Case 4 - zero dividend:
 * Input:
 * 0
 * 5
 * Expected output:
 * 0
 *
 * Case 5 - invalid second value:
 * Input:
 * 10
 * Java
 * Expected output:
 * java.util.InputMismatchException
 *
 * Case 6 - value outside the int range:
 * Input:
 * 2147483648
 * 1
 * Expected output:
 * java.util.InputMismatchException
 *
 * Note:
 * The exception message may vary between Java runtime versions.
 * The solution prints the actual exception object rather than
 * hard-coding a runtime-specific message.
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class JavaExceptionHandlingTryCatch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            int first = scanner.nextInt();
            int second = scanner.nextInt();
            int result = first / second;

            System.out.println(result);
        } catch (InputMismatchException exception) {
            System.out.println(
                exception.getClass().getName());
        } catch (ArithmeticException exception) {
            System.out.println(exception);
        }

        scanner.close();
    }
}