# Day 3 - Java Methods, ArrayList, and String Processing

## Objectives

- Practice defining and calling Java methods.
- Understand parameters, return values, `void`, scope, and basic `static` usage.
- Learn when and how to use `ArrayList`.
- Refactor task operations into separate methods.
- Add task creation, display, search, and deletion features.
- Practice string comparison, capitalization, and immutability.
- Complete the HackerRank Java Arraylist and Java Strings Introduction challenges.
- Continue practicing compilation, testing, debugging, and Git workflow.

## Completed tasks

- Synchronized the local `develop` branch with the remote repository.
- Created Issue #5 with a Day 3 checklist.
- Created and pushed the `feature/day03-methods-arraylist` branch.
- Created `MethodsDemo.java`.
- Practiced methods with parameters and return values.
- Practiced `void` methods and early returns.
- Reviewed local variable and parameter scope.
- Distinguished static methods from instance methods.
- Created `TaskManagerApp.java`.
- Stored task names in an `ArrayList<String>`.
- Separated menu, input, output, search, and deletion logic into methods.
- Added task creation, display, search, and deletion features.
- Added validation for empty input, invalid menu choices, non-integer input, and invalid task indexes.
- Used the VS Code debugger to inspect ArrayList contents before and after adding and deleting elements.
- Completed Java Arraylist and Java Strings Introduction.
- Created reusable `.txt` input files for local test cases.
- Used input redirection to execute programs with fixed test data.
- Added problem descriptions, solution approaches, complexity analyses, edge cases, and examples to the HackerRank source files.
- Reviewed ten Java and interview topics through guided discussion.
- Updated the Issue checklist after each completed stage.

## Java commands

Compile the methods demonstration:

```bash
javac -d task-manager/out \
    task-manager/src/com/interview/MethodsDemo.java
```

Run the methods demonstration:

```bash
java -cp task-manager/out com.interview.MethodsDemo
```

Compile the task manager:

```bash
javac -d task-manager/out \
    task-manager/src/com/interview/TaskManagerApp.java
```

Run the task manager:

```bash
java -cp task-manager/out com.interview.TaskManagerApp
```

Compile the Day 3 HackerRank exercises:

```bash
javac -d hacker-rank/day03/out \
    hacker-rank/day03/JavaArraylist.java \
    hacker-rank/day03/JavaStringsIntroduction.java
```

Run a program with a fixed input file:

```bash
java -cp hacker-rank/day03/out JavaArraylist \
    < hacker-rank/day03/test-data/java-arraylist-sample.txt
```

Check staged files for whitespace errors:

```bash
git diff --cached --check
```

## Task manager features

### Add task

What I practiced:

- Passing `Scanner` and `ArrayList<String>` to methods.
- Validating empty task names.
- Adding elements with `ArrayList.add()`.

### Display tasks

What I practiced:

- Checking empty-list behavior with `isEmpty()`.
- Iterating from index `0` to `size() - 1`.
- Converting zero-based indexes to one-based display numbers.
- Retrieving elements with `ArrayList.get()`.

### Search tasks

What I practiced:

- Reading and validating a search keyword.
- Searching task names with `String.contains()`.
- Tracking whether a matching task was found.

### Delete task

What I practiced:

- Converting string input with `Integer.parseInt()`.
- Handling `NumberFormatException`.
- Validating lower and upper task-number bounds.
- Converting a one-based task number to a zero-based index.
- Deleting and retrieving an element with `ArrayList.remove()`.

### Testing

Result:

- Normal task operations passed.
- Empty-list behavior passed.
- Empty task-name validation passed.
- Empty search-keyword validation passed.
- Invalid menu-choice validation passed.
- Invalid task-number validation passed.
- Non-integer task-number validation passed.
- ArrayList add and delete behavior was verified with the VS Code debugger.

## HackerRank exercises

### Java Arraylist

Challenge:

https://www.hackerrank.com/challenges/java-arraylist/problem

What I practiced:

- Creating an `ArrayList<ArrayList<Integer>>`.
- Creating and adding an inner list for every input row.
- Preserving rows that contain zero elements.
- Converting one-based query values to zero-based indexes.
- Validating outer and inner indexes.
- Using short-circuit evaluation to avoid invalid `get()` operations.
- Analyzing time and space complexity with separate input-size variables.

Complexity:

- Time: `O(R + E + Q)`
- Space: `O(R + E)`

Here, `R` is the number of rows, `E` is the total number of stored integers, and `Q` is the number of queries.

Result:

- Local compilation passed.
- The HackerRank example passed.
- The varied-row and empty-row test passed.
- The index-boundary test passed.
- All HackerRank test cases passed.

### Java Strings Introduction

Challenge:

https://www.hackerrank.com/challenges/java-strings-introduction/problem

What I practiced:

- Calculating string lengths.
- Comparing strings lexicographically with `compareTo()`.
- Distinguishing identity, equality, and ordering.
- Capitalizing the first letter with `substring()` and `toUpperCase()`.
- Understanding that String operations create new values because String is immutable.
- Analyzing string-processing complexity.

Complexity:

- Time: `O(A + B)`
- Extra space: `O(A + B)`

Here, `A` and `B` are the lengths of the two input strings.

Result:

- Local compilation passed.
- The example test passed.
- The lexicographically greater test passed.
- The equal-strings test passed.
- All HackerRank test cases passed.

## Day 3 reflection

Today I continued following a step-by-step implementation, testing, debugging, and Git workflow. Breaking the task manager into separate methods made it easier to understand which part of the program was responsible for each operation. It also made debugging more focused because individual methods and variables could be inspected without searching through all of the program logic at once.

The Java Arraylist exercise revealed that I was not yet familiar with constructing a nested ArrayList. I initially tried to retrieve an inner list before adding one to the outer list. I also used `break` when processing an empty row, which ended the entire row-reading loop and caused the remaining input to be interpreted incorrectly. Fixing these problems helped me understand the importance of preserving empty rows and distinguishing `break` from `continue`.

Several interview concepts still require more precise explanations. These include the relationship between static methods and classes, the relationship between instance methods and objects, Array and ArrayList operations, overloaded `remove()` methods, String identity and equality, Java pass-by-value, short-circuit evaluation, and complexity analysis. I also need more practice with `compareTo()` and `substring()`.

Creating reusable `.txt` test data made the testing process more efficient and repeatable. Instead of manually pasting the same input each time, I could run the program with input redirection and verify the same normal and boundary cases after every change. In the next practice session, I plan to try expected-output files and a batch test script that can automatically compare actual and expected results with `diff`.

The Git workflow and quality checks are becoming more familiar, but the checkpoint process can still be made more efficient. Future checkpoints should provide the required paths and commands at the current stage, clearly distinguish the state before and after `git add`, and avoid requiring old terminal output. Repeated mistakes and important interview concepts should also be recorded as review notes so they can be revisited in later practice.