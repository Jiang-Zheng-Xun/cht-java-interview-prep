/*
 * HackerRank: Java Inheritance I
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-inheritance-1/problem
 *
 * Problem:
 * Animal provides a walk() method. Create a Bird subclass that
 * inherits walk(), provides fly(), and adds sing().
 *
 * Input:
 * This challenge does not require input.
 *
 * Output:
 * Print the following actions in order:
 * 1. I am walking
 * 2. I am flying
 * 3. I am singing
 *
 * Approach:
 * - Define Animal as the superclass.
 * - Define Bird as a subclass using extends.
 * - Let Bird inherit walk() from Animal.
 * - Add Bird-specific fly() and sing() methods.
 * - Create a Bird object and call all three methods.
 *
 * Complexity:
 * Let M be the number of method calls made by main.
 *
 * - Time: O(M), which is O(1) because M is fixed at three.
 * - Extra space: O(1).
 *
 * Important edge cases:
 * - Bird should inherit walk() instead of redefining it.
 * - sing() must belong to Bird.
 * - The three output lines must appear in the required order.
 * - Output text and capitalization must match exactly.
 *
 * Example output:
 * I am walking
 * I am flying
 * I am singing
 */

class Animal {
    public void walk() {
        System.out.println("I am walking");
    }
}

class Bird extends Animal {
    public void fly() {
        System.out.println("I am flying");
    }

    public void sing() {
        System.out.println("I am singing");
    }
}

public class JavaInheritanceI {
    public static void main(String[] args) {
        Bird bird = new Bird();

        bird.walk();
        bird.fly();
        bird.sing();
    }
}