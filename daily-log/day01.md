# Day 1 - Environment Setup and Java Basics

## Objectives

- Set up the Java development environment on Windows 11 and WSL2.
- Configure Git and connect the local repository to GitHub.
- Practice a feature branch workflow.
- Compile, run, and debug the first Java program.

## Environment

- Windows 11
- WSL2
- Ubuntu 22.04.5 LTS
- zsh
- Microsoft OpenJDK 21 on Windows
- OpenJDK 21 on WSL
- Visual Studio Code with WSL and Java extensions
- Git and GitHub

## Completed tasks

- Created the GitHub repository `cht-java-interview-prep`.
- Cloned the repository into the WSL filesystem.
- Configured the Git user name, email, default branch, and line endings.
- Created the `develop` branch.
- Created the `feature/day01-setup` branch.
- Created the initial project directory structure.
- Wrote and compiled `HelloWorld.java`.
- Ran the program from the WSL terminal.
- Ran and debugged the program using VS Code.
- Verified that compiled `.class` files are ignored by Git.

## Java commands

Compile:

```bash
javac -d task-manager/out task-manager/src/com/interview/HelloWorld.java

## HackerRank exercises

### Java Stdin and Stdout I

Challenge:

https://www.hackerrank.com/challenges/java-stdin-and-stdout-1/problem

What I practiced:

- Reading integer input with `Scanner`.
- Storing input values in `int` variables.
- Printing values with `System.out.println()`.
- Compiling and running a Java class from the WSL terminal.

Result:

- Local compilation passed.
- Local test cases passed.
- All HackerRank test cases passed.

### Java If-Else

Challenge:

https://www.hackerrank.com/challenges/java-if-else/problem

What I practiced:

- Determining whether an integer is odd or even with the modulo operator.
- Using `if`, `else if`, and `else`.
- Combining numeric range conditions with logical operators.
- Testing boundary values.

Result:

- Local compilation passed.
- Boundary tests passed.
- All HackerRank test cases passed.

## Day 1 reflection

Today I completed the Java development environment setup and practiced the basic Java workflow from source code to execution. I compiled programs with `javac`, ran compiled classes with `java`, and used VS Code to run and debug Java code in WSL.

I also practiced reading standard input and implementing conditional logic through two HackerRank exercises. The environment and Git problems encountered today reinforced the importance of checking the active shell, understanding file paths, and verifying which generated files are ignored before committing code.