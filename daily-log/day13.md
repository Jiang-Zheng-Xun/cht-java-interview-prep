# Day 13 - SQL Basics and Maven Testing

## Overview

Day 13 focused on setting up a Maven and JUnit testing workflow, learning SQL fundamentals with SQLite, designing a small Task Manager schema, and completing a HackerRank SQL Basic Select challenge.

The day also included troubleshooting the VS Code Java project model, validating database constraints with deliberate failures, and reviewing SQL fundamentals through six interview questions.

## Environment Setup

### Maven

Maven was not installed at the beginning of Day 13.

The Ubuntu package was inspected before installation:

```text
Maven candidate: 3.6.3-5
Source: Ubuntu 22.04 jammy/universe
```

The simulated installation showed:

```text
0 upgraded
33 newly installed
0 removed
```

Maven was then installed and verified:

```text
Apache Maven 3.6.3
Java version: 21.0.11
Java home: /usr/lib/jvm/java-21-openjdk-amd64
```

No additional `M2_HOME` or `PATH` configuration was required.

### SQLite

SQLite was installed after inspecting the Ubuntu package and simulating the installation.

Verified environment:

```text
SQLite: 3.37.2
Java: 21.0.11
Maven: 3.6.3
```

A smoke test used an in-memory database:

```text
:memory:
```

An in-memory SQLite database exists only for the lifetime of the `sqlite3` process and does not create a database file in the repository.

## Maven and JUnit Testing

### Maven Project Configuration

The existing Task Manager project does not use the standard Maven directory layout.

The project currently uses:

```text
task-manager/src
task-manager/test/src
```

A `task-manager/pom.xml` was created with explicit source directories:

```xml
<sourceDirectory>src</sourceDirectory>
<testSourceDirectory>test/src</testSourceDirectory>
```

The project uses:

```text
Java release: 21
JUnit Jupiter: 5.10.2
Maven Compiler Plugin: 3.11.0
Maven Surefire Plugin: 3.2.5
```

JUnit is declared with test scope so it is available to tests without becoming a production runtime dependency.

### JUnit Parameterized Test

`TaskCommandValidatorJUnitTest.java` was added with 17 parameterized test cases.

Final result:

```text
Tests run: 17
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
Exit code: 0
```

A deliberate failure changed the expected result of the `COMPLETE 0` test from `INVALID` to `COMPLETE`.

The failure result was:

```text
Tests run: 17
Failures: 1
Errors: 0
Skipped: 0
expected: <COMPLETE>
but was: <INVALID>
BUILD FAILURE
Exit code: 1
```

The expected value was immediately restored to `INVALID`, after which all 17 tests passed again.

This confirmed both sides of the testing workflow:

* correct behavior allows the build to pass
* an incorrect expectation causes a visible test failure and non-zero exit code

### Maven Build Output

The repository originally ignored `.class` files but did not ignore every file created under Maven `target/` directories.

The root `.gitignore` was updated with:

```gitignore
target/
```

This excludes compiled classes, Surefire XML reports, text reports, and other Maven build output.

## VS Code Project Model Diagnosis

Day 12 used `.vscode/settings.json` to configure:

```json
{
    "java.project.sourcePaths": [
        "task-manager/src",
        "task-manager/test/src"
    ],
    "java.project.referencedLibraries": []
}
```

After the Maven project was created, VS Code still treated the source folders as an unmanaged Java project. Maven command-line tests succeeded, but the editor displayed diagnostics such as:

```text
The import org.junit cannot be resolved
Arguments cannot be resolved
The method assertEquals(...) is undefined
```

Maven dependency inspection confirmed that all required JUnit JAR files existed in the local Maven repository.

The local VS Code settings were changed to:

```json
{}
```

The Java Language Server workspace was cleaned and restarted. VS Code then imported the Maven project model, displayed Maven dependencies, and removed the JUnit diagnostics.

The local `.vscode/settings.json` remains excluded through `.git/info/exclude` and is not part of the repository changes.

Key distinction:

* source paths determine whether development tools can find and organize source code
* Java packages and access modifiers determine whether Java permits one class to access another

A static method such as:

```java
TaskCommandValidator.identify(input);
```

is called through the class and does not require a `TaskCommandValidator` instance. The `input` value is a method argument, not an instance.

The original `TaskCommandValidatorTest` was a custom Java testing harness and did not use a third-party testing framework. The new JUnit test introduced the third-party testing framework.

## Task Manager SQL Basics

The SQL exercise created:

```text
task-manager/sql/day13/task-manager-basics.sql
```

The script contains:

* `users` and `tasks` tables
* primary keys
* a foreign key
* `NOT NULL`
* `UNIQUE`
* `DEFAULT`
* `CHECK`
* sample data
* eight basic queries

### Table Relationship

The relationship is one-to-many:

```text
one user -> many tasks
one task -> one user
```

Key responsibilities:

```text
users.id
    primary key that uniquely identifies a user

tasks.id
    primary key that uniquely identifies a task

tasks.user_id
    foreign key that references users.id
```

`tasks.user_id` must be `NOT NULL` when every task is required to belong to a user.

A foreign key maintains referential integrity but does not automatically prevent two tasks from having the same content.

### Username Constraints

The username uses:

```sql
username TEXT NOT NULL UNIQUE
    CHECK (length(trim(username)) > 0)
```

The responsibilities are separate:

* `NOT NULL` rejects SQL `NULL`
* `UNIQUE` rejects duplicate usernames
* `CHECK` rejects empty strings and strings containing only whitespace

`NULL`, an empty string, and a whitespace-only string are different:

```text
NULL   = unknown, missing, or not provided
''     = a known zero-length string
'   '  = a known string containing whitespace
```

### Task Status and Priority

Task status uses:

```sql
DEFAULT 'PENDING'
CHECK (
    status IN (
        'PENDING',
        'IN_PROGRESS',
        'COMPLETED'
    )
)
```

Task priority uses:

```sql
CHECK (priority BETWEEN 1 AND 5)
```

`BETWEEN 1 AND 5` includes both endpoints.

### Constraint Failure Tests

Four invalid inserts were deliberately tested:

```text
duplicate username
    UNIQUE constraint failed
    exit code 1

nonexistent user_id
    FOREIGN KEY constraint failed
    exit code 1

invalid status
    CHECK constraint failed
    exit code 1

whitespace-only username
    CHECK constraint failed
    exit code 1
```

The valid SQL regression test returned exit code `0`.

### SQL Query Practice

The exercise practiced:

* `SELECT`
* `WHERE`
* `ORDER BY`
* `AND`
* `OR`
* `IN`
* `BETWEEN`
* `LIKE`
* `IS NULL`
* `IS NOT NULL`

To place tasks with a non-NULL due date before tasks without one, the query used:

```sql
ORDER BY
    due_date IS NULL,
    due_date
```

In SQLite, `due_date IS NULL` evaluates to `0` for non-NULL values and `1` for NULL values. Ascending order places `0` before `1`.

`due_date IS NULL` only affects sorting when used in `ORDER BY`. In a `WHERE` clause, it filters rows instead.

### AND and OR Precedence

SQL evaluates `AND` before `OR`.

The intended condition was:

```sql
WHERE status = 'PENDING'
  AND (
      priority IN (1, 2)
      OR due_date IS NULL
  )
```

This returned task IDs:

```text
1, 5, 6
```

Without parentheses:

```sql
WHERE status = 'PENDING'
  AND priority IN (1, 2)
  OR due_date IS NULL
```

SQL interprets the condition as:

```text
(status is PENDING AND priority is 1 or 2)
OR due date is NULL
```

This returned:

```text
1, 3, 5, 6
```

Task 3 was included even though it was `IN_PROGRESS`, because its due date was `NULL`.

Parentheses therefore express the intended business rule and are not only formatting.

## HackerRank SQL

Challenge:

```text
Revising the Select Query I
```

Dialect:

```text
MySQL
```

The query selected all columns for American cities with populations strictly larger than `100000`.

HackerRank result:

```text
Test case 0: Success
```

Local boundary tests confirmed:

* `100001` is accepted
* `100000` is excluded
* `99999` is excluded
* a non-USA city is excluded even when its population is larger than `100000`

The local regression test returned exit code `0`.

## Interview Review

Interview Review time:

```text
Start: 17:33:24 CST
Completion: 18:49:16 CST
Total elapsed time: 1 hour, 15 minutes, 52 seconds
```

Pure answer time:

| Question                             |      Time |
| ------------------------------------ | --------: |
| Primary key, foreign key, and unique |      6:18 |
| AND, OR, and parentheses             |      6:18 |
| NULL and three-valued logic          |      5:07 |
| LIKE, IN, and BETWEEN                |      3:29 |
| Constraints and data integrity       |      8:43 |
| Input validation and SQL injection   |      6:11 |
| **Total**                            | **36:06** |

The pure answer time was within the intended 30-to-45-minute range.

The full Interview Review time includes answering, reading feedback, correcting concepts, and reviewing explanations. The full elapsed time counts as effective training time.

### Stronger Topics

* `NULL` and three-valued logic
* `LIKE`, `IN`, and `BETWEEN`
* `AND` and `OR` grouping
* the core idea of fixing SQL structure and binding data values

### Topics Requiring More Practice

* `tasks.id` versus `tasks.user_id`
* primary key, foreign key, and unique responsibilities
* SQL values versus identifiers
* input validation, parameterized queries, and database constraints
* SQL injection defenses

## Input Validation, Parameterized Queries, and Constraints

These mechanisms have different responsibilities:

```text
Input validation
    checks format, length, allowed values, and business rules

Parameterized query
    separates a fixed SQL structure from externally supplied data values

Database constraint
    protects the integrity of data written through every access path
```

Application validation can provide early and user-friendly errors, but cannot replace database constraints. Data may be written by another service, migration script, administration tool, concurrent request, or application version with missing validation.

Database constraints are the final shared data-integrity boundary.

A parameterized query can bind values:

```sql
SELECT *
FROM tasks
WHERE status = ?
```

The parameter can contain a value such as `PENDING`.

Table names, column names, and SQL keywords are identifiers or query structure and usually cannot be replaced by value parameters. Dynamic identifiers should come from fixed application choices or an allowlist.

`PreparedStatement` will be practiced again with concrete Java code on Day 15.

## Learner-led Reflection

### Most Valuable Learning

The most valuable topics included:

* source path versus Java package and access control
* SQLite in-memory databases
* `NOT NULL` versus empty strings
* using `CHECK` to reject blank usernames
* sorting NULL values explicitly
* separating validation, parameterization, and constraints

### Most Difficult Areas

The most demanding portion was the Maven and JUnit setup.

It required:

* package inspection
* Maven installation
* POM configuration
* dependency resolution
* deliberate failure verification
* VS Code project-model diagnosis
* Java Language Server cleanup

SQL was also a new topic, so understanding schema structure, syntax composition, and constraints required additional time.

### Workload Evaluation

Day 13 workload was:

```text
slightly too much
```

The original plan did not fully account for the time required to install, configure, integrate, troubleshoot, and understand multiple new tools while also beginning a new SQL topic.

### Process Feedback

Future diagnostic checklists should request observable facts only.

Concept questions such as:

* whether a static method needs an instance
* whether a test uses a third-party framework

should be asked separately rather than being embedded in a diagnostic report template.

For a completely new tool or language topic, the preferred sequence is:

```text
short introduction
minimum working implementation
concept questions based on the implementation
```

This approach supports learning through concrete experience before requiring abstract explanations.

When a new environment must be installed, the schedule should explicitly reserve time for:

* installation
* dependency download
* configuration
* IDE integration
* troubleshooting
* concept review

## Process Decisions

The following adjustments were accepted:

1. Keep diagnostic checklists focused on observable facts.
2. Ask core concept questions separately.
3. Use implementation-first learning for unfamiliar tools and languages.
4. Reserve explicit time for new environment setup and troubleshooting.
5. Review `PreparedStatement` with concrete Java code on Day 15.
6. Do not install another new tool on Day 14.
7. Record Day 13 workload as slightly too much.

## Git Record

Day 13 commits before the Daily Log:

```text
521ff27 build: add Maven and JUnit testing
4de6a56 feat: add Task Manager SQL basics
f4344ae feat: complete SQL Basic Select challenge
```

## Time Record

Important stages:

```text
Day 13 start: 08:56:44 CST
Feature branch ready: 09:11:52 CST
Maven and JUnit commit pushed: 11:33:17 CST
Lunch: 11:46:07–14:52:10 CST
Task Manager SQL commit pushed: 16:59:26 CST
HackerRank completed: 17:23:29 CST
HackerRank commit pushed: 17:32:31 CST
Interview Review: 17:33:24–18:49:16 CST
Dinner: 18:53:36–19:47:14 CST
Reflection: 19:48:50–20:22:18 CST
```

Current effective training time through Reflection completion:

```text
Morning and afternoon:
08:56:44–18:53:36
minus lunch 11:46:07–14:52:10
= 6 hours, 50 minutes, 49 seconds

Evening before Daily Log:
19:47:14–20:22:18
= 35 minutes, 4 seconds

Current total:
7 hours, 25 minutes, 53 seconds
```

This is a provisional value. Daily Log creation, final checks, pull request, merge, local cleanup, and Issue closure will be added to the final effective training time.
