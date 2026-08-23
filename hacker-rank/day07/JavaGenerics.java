/*
 * HackerRank: Java Generics
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-generics/problem
 *
 * Problem:
 * Create one generic method named printArray that can print
 * every element from arrays containing different reference
 * types.
 *
 * The same method must work with both Integer[] and String[].
 * Method overloading is not allowed.
 *
 * Input format:
 * This challenge does not read input from standard input.
 * The arrays are created by the provided program.
 *
 * Required behavior:
 * - Print each element on its own line.
 * - Use one generic printArray method.
 * - Do not create separate overloaded methods for Integer[]
 *   and String[].
 *
 * Official arrays:
 * Integer[]: 1, 2, 3
 * String[]: Hello, World
 *
 * Official expected output:
 * 1
 * 2
 * 3
 * Hello
 * World
 *
 * Additional local test 1:
 * Double[]: 1.5, 2.5
 *
 * Expected output:
 * 1.5
 * 2.5
 *
 * Additional local test 2:
 * Character[]: A, B
 *
 * Expected output:
 * A
 * B
 *
 * Planned approach:
 * - Declare one generic method with method type parameter T.
 * - Accept an array of T elements.
 * - Use an enhanced for loop to visit every element.
 * - Print each element with System.out.println().
 *
 * Complexity:
 * Let N be the number of elements in the input array.
 *
 * - Time: O(N), because every element is visited once.
 * - Extra space: O(1), excluding output storage.
 *
 * Important edge cases:
 * - A one-element array must print exactly one line.
 * - An empty array should print nothing.
 * - The method accepts arrays of reference types such as
 *   Integer[], String[], Double[], and Character[].
 * - A primitive array such as int[] cannot be passed to T[]
 *   because Java generic type arguments cannot be primitive
 *   types.
 * - Null arrays and null elements are outside the official
 *   challenge requirements.
 *
 *  * HackerRank editor note:
 * The Run Code skeleton used during this practice only
 * provided imports and an empty Solution.main().
 *
 * Therefore, the submission had to create the Printer class,
 * the generic printArray method, both arrays, the Printer
 * object, and both method calls.
 *
 * The prohibition against method overloading was part of the
 * challenge contract and grader checks, not visible reflection
 * code in the editor skeleton.
 *
 */

class Printer {
    public <T> void printArray(T[] array) {
       for(T element: array){
        System.out.println(element);
       }

        /*
        * Old version valid Printer:
            for(int index=0; index < array.length; index++ ){
                System.out.println(array[index]);
            }
        *
        */
    }
}

public class JavaGenerics {
    public static void main(String[] args) {
        Printer printer = new Printer();

        if (args.length == 1
                && args[0].equals("local")) {
            Double[] doubleArray = {1.5, 2.5};
            Character[] characterArray = {'A', 'B'};

            printer.printArray(doubleArray);
            printer.printArray(characterArray);
            return;
        }

        Integer[] integerArray = {1, 2, 3};
        String[] stringArray = {"Hello", "World"};

        printer.printArray(integerArray);
        printer.printArray(stringArray);
    }
}