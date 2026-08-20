/*
 * HackerRank: Java Method Overriding 2 (Super Keyword)
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-method-overriding-2-super-keyword/problem
 *
 * Problem:
 * BiCycle defines define_me(). MotorCycle extends BiCycle and
 * overrides the same method. Complete MotorCycle so that it can
 * use both its own implementation and the superclass version.
 *
 * Input:
 * This challenge does not require input.
 *
 * Output:
 * Print the MotorCycle description, followed by the description
 * returned from its superclass.
 *
 * Approach:
 * - Define BiCycle as the superclass.
 * - Define MotorCycle as a subclass of BiCycle.
 * - Override define_me() in MotorCycle.
 * - Call the current object's overridden method.
 * - Use the super keyword to call the superclass implementation.
 *
 * Complexity:
 * Let M be the number of method calls.
 *
 * - Time: O(M), which is O(1) because M is fixed.
 * - Extra space: O(1).
 *
 * Important edge cases:
 * - A normal define_me() call uses the MotorCycle implementation.
 * - The superclass implementation must be called explicitly.
 * - super refers to superclass behavior, not a separate object.
 * - Output text, punctuation, and order must match exactly.
 *
 * Example output:
 * Hello I am a motorcycle, I am a cycle with an engine.
 * My ancestor is a cycle who is a vehicle with pedals.
 */

class BiCycle {
    public String define_me() {
        return "a vehicle with pedals.";
    }
}

class MotorCycle extends BiCycle {
    @Override
    public String define_me() {
        return "a cycle with an engine.";
    }

    public MotorCycle() {
        System.out.println(
                "Hello I am a motorcycle, I am "
                        + define_me());

        String ancestorDefinition = null;
        ancestorDefinition = super.define_me();

        System.out.println(
                "My ancestor is a cycle who is "
                        + ancestorDefinition);
    }
}

public class JavaMethodOverriding2 {
    public static void main(String[] args) {
        new MotorCycle();
    }
}