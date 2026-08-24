/*
 * HackerRank: Java Sort
 *
 * Challenge:
 * https://www.hackerrank.com/challenges/java-sort/problem
 *
 * Problem:
 * Read information about students. Each student has:
 *
 * - A unique integer ID.
 * - A lowercase first name.
 * - A CGPA value.
 *
 * Sort the students using these rules:
 *
 * 1. Higher CGPA comes first.
 * 2. If CGPA values are equal, sort by first name in
 *    alphabetical order.
 * 3. If both CGPA and first name are equal, sort by ID in
 *    ascending order.
 *
 * Print only the first name of each student after sorting.
 *
 * Approach:
 * - Store each input row as a Student object.
 * - Build one Comparator with three comparison levels.
 * - Sort the List with List.sort().
 * - Print each student's first name.
 *
 * Complexity:
 * Let N be the number of students.
 *
 * - Reading and storing students: O(N).
 * - Sorting: O(N log N) in the general worst case.
 * - Printing: O(N).
 * - Total time: O(N log N).
 * - Stored student data: O(N).
 * - Sorting may require O(N) auxiliary space in the
 *   worst case.
 *
 * Important edge cases:
 * - Multiple students can have the same CGPA.
 * - Multiple students can also have the same first name.
 * - ID must be used only after both earlier comparisons tie.
 * - CGPA order is descending, but name and ID are ascending.
 * - Only first names are printed.
 *
 * Official sample input:
 * 5
 * 33 Rumpa 3.68
 * 85 Ashis 3.85
 * 56 Samiha 3.75
 * 19 Samara 3.75
 * 22 Fahim 3.76
 *
 * Official sample output:
 * Ashis
 * Fahim
 * Samara
 * Samiha
 * Rumpa
 *
 * Local test: all comparison levels
 *
 * Input:
 * 6
 * 30 bob 3.50
 * 20 alice 3.50
 * 10 alice 3.50
 * 40 zoe 4.00
 * 50 mike 2.75
 * 60 anna 3.75
 *
 * Expected output:
 * zoe
 * anna
 * alice
 * alice
 * bob
 * mike
 *
 * Note:
 * The two alice output lines look identical, so the printed
 * output alone cannot prove their internal ID order. The
 * comparator definition must still include ascending ID as
 * its third comparison rule.
 *
 * Local test: boundary CGPA and name tie
 *
 * Input:
 * 4
 * 10 bob 4.00
 * 5 alice 4.00
 * 7 carol 0.00
 * 3 dave 3.99
 *
 * Expected output:
 * alice
 * bob
 * dave
 * carol
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Student {
    private final int id;
    private final String firstName;
    private final double cgpa;

    Student(int id, String firstName, double cgpa) {
        this.id = id;
        this.firstName = firstName;
        this.cgpa = cgpa;
    }

    int getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    double getCgpa() {
        return cgpa;
    }
}

public class JavaSort {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int studentCount = scanner.nextInt();
        List<Student> students = new ArrayList<>();

        for (int index = 0;
                index < studentCount;
                index++) {
            int id = scanner.nextInt();
            String firstName = scanner.next();
            double cgpa = scanner.nextDouble();

            students.add(
                    new Student(id, firstName, cgpa));
        }

        Comparator<Student> studentComparator =
                Comparator.comparingDouble(Student::getCgpa)
                        .reversed()
                        .thenComparing(Student::getFirstName)
                        .thenComparingInt(Student::getId);

        students.sort(studentComparator);

        for (Student student : students) {
            System.out.println(student.getFirstName());
        }

        scanner.close();
    }
}