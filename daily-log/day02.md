# Day 2 - Java Basics, Loops, and Arrays

## Objectives

- Review Java primitive and reference types.
- Practice loops, arrays, and standard input handling.
- Build a task list program with input validation.
- Complete the HackerRank Java Loops I and Java Loops II challenges.
- Continue practicing compilation, testing, debugging, and Git workflow.

## Completed tasks

- Synchronized the local `develop` branch with the remote repository.
- Created the `feature/day02-java-basics` branch.
- Created `TaskListDemo.java`.
- Read task quantities and task names with `Scanner`.
- Stored task names in a `String` array.
- Handled the newline left by `nextInt()` before using `nextLine()`.
- Added validation for invalid quantities and empty task names.
- Displayed the completed task list with a loop.
- Compiled and tested the program with normal, boundary, and invalid inputs.
- Used the VS Code debugger to inspect program variables.
- Completed Java Loops I and Java Loops II.
- Verified that generated `.class` files and `out/` directories are ignored by Git.
- Reviewed ten Java basics and interview questions through guided discussion.

## Java commands

Compile `TaskListDemo.java`:

```bash
javac -d task-manager/out \
    task-manager/src/com/interview/TaskListDemo.java
```

Run `TaskListDemo`:

```bash
java -cp task-manager/out com.interview.TaskListDemo
```

Compile the Day 2 HackerRank exercises:

```bash
javac -d hacker-rank/day02/out \
    hacker-rank/day02/JavaLoopsI.java \
    hacker-rank/day02/JavaLoopsII.java
```

Check for whitespace errors before committing:

```bash
git diff --cached --check
```

## HackerRank exercises

### Java Loops I

Challenge:

https://www.hackerrank.com/challenges/java-loops-i/problem

What I practiced:

- Using a `for` loop with a fixed number of iterations.
- Performing repeated multiplication.
- Producing formatted output with the required structure.
- Compiling and testing a Java program from the WSL terminal.

Result:

- Local compilation passed.
- Local test cases with different input values passed.
- All HackerRank test cases passed.

### Java Loops II

Challenge:

https://www.hackerrank.com/challenges/java-loops/problem

What I practiced:

- Using nested loops to process multiple queries.
- Maintaining a cumulative sum inside a loop.
- Updating powers of two without recalculating previous terms.
- Formatting multiple results with spaces and line breaks.
- Testing normal and boundary cases.

Result:

- Local compilation passed.
- The provided example test passed.
- The single-term test passed.
- The test with `b = 0` passed.
- All HackerRank test cases passed.

## Day 2 reflection

Today I carefully followed the planned implementation, compilation, testing, debugging, and Git steps. Instead of only making the programs run, I used normal, boundary, and invalid inputs to verify their behavior. This helped me rebuild my debugging habits and understand why a repeatable development and testing process will become increasingly important as programs grow larger and more complex.

The guided review also revealed several areas where my Java terminology and explanations were not yet precise. I initially mixed terminology from other programming languages with Java concepts, such as calling methods functions, treating references like pointers, and confusing object identity with content equality. I also learned to distinguish the default values of array elements from the initialization requirements of local variables.

I gained a clearer understanding of when to use arrays and `ArrayList`, and I identified areas that require more practice, including `StringBuilder`, reference comparison, and the behavior of `Scanner.nextInt()` followed by `nextLine()`. These issues were generally caused by applying knowledge from other languages without first checking Java-specific behavior.

In future practice, I will record concepts or operations when the same mistake appears more than once. I will also organize frequently used commands by purpose so they are easier to review. Although the workflow between the local repository and GitHub is becoming familiar, I still need to understand the exact purpose of each Git and quality-checking command. A visual workflow that connects each project-management step with its corresponding command may help reinforce this understanding through continued practice.