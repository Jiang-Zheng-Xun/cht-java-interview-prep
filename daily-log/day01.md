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