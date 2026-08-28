/*
 * HackerRank: Java Anagrams
 *
 * Problem:
 * Determine whether two strings contain the same English letters
 * with the same frequencies, ignoring case.
 *
 * Requirements:
 * - Complete isAnagram(String a, String b).
 * - Return a boolean.
 * - The comparison is case-insensitive.
 * - Inputs contain English alphabetic characters.
 *
 * Local execution:
 * - This file uses JavaAnagrams as the public class name.
 * - Rename JavaAnagrams to Solution in the HackerRank editor.
 */

import java.util.Scanner;

public class JavaAnagrams {
    public static boolean isAnagram(
            String a,
            String b) {
        if (a == null || b == null) {
            return false;
        }

        if (a.length() != b.length()) {
            return false;
        }

        int[] frequencies = new int[26];

        for (int i = 0; i < a.length(); i++) {
            char characterFromA =
                    Character.toLowerCase(a.charAt(i));

            char characterFromB =
                    Character.toLowerCase(b.charAt(i));

            frequencies[characterFromA - 'a']++;
            frequencies[characterFromB - 'a']--;
        }

        for (int frequency : frequencies) {
            if (frequency != 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            if (!scanner.hasNextLine()) {
                return;
            }

            String a = scanner.nextLine();

            if (!scanner.hasNextLine()) {
                return;
            }

            String b = scanner.nextLine();

            System.out.println(
                    isAnagram(a, b)
                            ? "Anagrams"
                            : "Not Anagrams"
            );
        }
    }
}