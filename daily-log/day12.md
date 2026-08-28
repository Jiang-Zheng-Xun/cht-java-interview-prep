# Day 12 — Java Testing and Integration Review

## Goals

- Practice Java testing fundamentals.
- Refactor validation logic into testable production code.
- Separate production code, demonstration code, and test code.
- Apply Arrange–Act–Assert.
- Design normal, boundary, and invalid test cases.
- Compare manual console checks with automated assertions.
- Complete the HackerRank Java Anagrams challenge.
- Complete the third valid workload observation.

## Environment Decision

Maven was not installed:

```text
command -v mvn
exit code: 1
```

No unplanned installation was performed.

A lightweight Java test harness was used instead. If Maven is introduced later, it will use a separate guided setup covering installation, version verification, project structure, dependencies, test execution, and persistence after restarting the environment.

## Main Implementation

### Responsibility Separation

Day 11 originally placed regex rules, validation, sample inputs, and output formatting in:

```text
TaskInputValidationDemo.java
```

Day 12 separated the responsibilities:

```text
TaskCommandValidator
→ stores regex and validation logic
→ returns CommandType

TaskInputValidationDemo
→ prepares sample inputs
→ calls the validator
→ formats console output

TaskCommandValidatorTest
→ prepares expected values
→ calls the validator
→ compares expected with actual
```

This avoids testing validation logic by parsing console formatting.

### TaskCommandValidator

Created:

```text
task-manager/src/com/interview/TaskCommandValidator.java
```

The validator contains reusable patterns and returns an enum:

```java
public enum CommandType {
    ADD,
    COMPLETE,
    LIST,
    INVALID
}
```

Public API:

```java
public static CommandType identify(String input)
```

`null` is explicitly handled as `INVALID`.

The class is `final` and has a private constructor because it is used as a static utility class.

### Refactored Demo

Modified:

```text
task-manager/src/com/interview/TaskInputValidationDemo.java
```

The Demo no longer imports or stores `Pattern` and `Matcher`. It calls:

```java
TaskCommandValidator.identify(input)
```

and converts `CommandType` into display text.

The Day 11 output remained unchanged after the refactor.

### Lightweight Test Harness

Created:

```text
task-manager/test/src/com/interview/TaskCommandValidatorTest.java
```

The harness includes:

- 17 automated test cases
- normal, boundary, null, empty, and invalid inputs
- expected and actual comparison
- PASS and FAIL counters
- diagnostic output containing test name, input, expected, and actual
- non-zero exit code when any test fails

Successful result:

```text
Summary: 17 passed, 0 failed
exit code: 0
```

A deliberate failure was introduced by temporarily changing the expected result for `"ADD Review regex"`.

Observed result:

```text
16 passed, 1 failed
exit code: 1
```

The expected value was restored, and the final regression run returned:

```text
17 passed, 0 failed
exit code: 0
```

This verified both the success and failure paths of the test harness.

Commit:

```text
f5fc0f1 feat: add task command validator tests
```

## VS Code Source Root Diagnosis

The initial test path was:

```text
task-manager/test/com/interview/TaskCommandValidatorTest.java
```

VS Code reported:

```text
The declared package "com.interview"
does not match the expected package ""
```

The file was moved to:

```text
task-manager/test/src/com/interview/TaskCommandValidatorTest.java
```

The obsolete empty directories were removed.

The package error remained because the unmanaged Java project did not recognize the source roots.

The final source roots were configured through:

```text
Ctrl+Shift+P
→ Java: Configure Classpath
→ Classpath Sources
→ Add Source Root
→ Select Source Folder
→ Apply Settings
```

Configured paths:

```json
{
    "java.project.sourcePaths": [
        "task-manager/src",
        "task-manager/test/src"
    ],
    "java.project.referencedLibraries": []
}
```

The configuration was stored in:

```text
.vscode/settings.json
```

It is local workspace configuration and was excluded through:

```text
.git/info/exclude
```

Entry:

```text
.vscode/
```

The setting remains available locally but is not committed or added to the shared `.gitignore`.

After reloading VS Code:

- the package diagnostic disappeared
- `TaskCommandValidator` resolved correctly
- `javac` succeeded
- all 17 tests passed

## GitHub Push Recovery

The first push of commit `f5fc0f1` failed:

```text
remote rejected:
Internal Server Error
```

Diagnosis showed:

- the local branch was ahead by one commit
- the working tree was clean
- `git ls-remote` succeeded
- the remote branch still pointed to the previous commit
- there was no divergence or conflict

A single controlled retry succeeded.

The evidence supports a temporary GitHub server-side failure rather than a code, commit, permission, or branch problem.

## HackerRank Java Anagrams

Official problem:

```text
Java Anagrams
```

Created:

```text
hacker-rank/day12/JavaAnagrams.java
```

The actual Java 15 editor contained only a minimal `Solution` and `main` skeleton.

The official contract requires:

- implement `isAnagram(String a, String b)`
- return `boolean`
- compare without case sensitivity
- inputs contain English alphabetic characters
- output `Anagrams` or `Not Anagrams`

### Frequency-counting Algorithm

A fixed array represents the 26 English letters:

```java
int[] frequencies = new int[26];
```

Character mapping:

```java
character - 'a'
```

The first string increments counts, and the second string decrements counts.

All 26 entries must be zero for the strings to be anagrams.

Complexity:

```text
Time:  O(n + 26) = O(n)
Space: O(26) = O(1)
```

The space is constant because the array size does not grow with input length. It should not be described as a Java `const` declaration.

The 26-position assumption depends on the problem constraint that inputs contain English alphabetic characters. It would not directly support arbitrary Unicode characters.

Tests covered:

- same frequencies in different order
- case differences
- same length but different characters
- different frequencies
- different lengths
- repeated characters

All official samples and local boundary tests passed.

HackerRank Java 15 submission:

```text
Test cases 0–16 passed
```

Commit:

```text
d92aa25 feat: complete Java Anagrams challenge
```

## Testing Concepts

### Unit Test

A unit test directly verifies a small, isolated behavior.

Today:

```text
TaskCommandValidatorTest
→ TaskCommandValidator.identify()
→ compare expected and actual
```

### Integration Test

An integration test checks whether multiple components work together.

Today, the Demo calling the Validator and converting `CommandType` into output is an integration-level check. Because the console result is still inspected manually, it is not a fully automated integration test.

### Regression Test

Regression describes the purpose of rerunning tests after a change to ensure existing behavior was not broken.

It is not a separate testing level parallel to unit and integration tests.

Today, the following had regression purposes:

- rerunning the Day 11 Demo after refactoring
- rerunning all 17 validator tests
- rerunning the HackerRank official sample before commit

A regression test can itself be a unit test or an integration test.

### Arrange–Act–Assert

For `"COMPLETE 0"`:

```text
Arrange:
input = "COMPLETE 0"
expected = INVALID

Act:
actual = TaskCommandValidator.identify(input)

Assert:
compare actual with expected
```

The test harness records PASS or FAIL; the entire process uses its exit code to indicate success or failure.

### Test Case and Test Fixture

A test case is a complete executable scenario containing:

- test purpose
- input
- tested behavior
- expected result

`"ADD A"` alone is only test input.

The complete case is:

```text
name: single-character ADD title
input: "ADD A"
act: identify(input)
expected: ADD
```

A test fixture is shared setup, state, data, or dependencies used by multiple tests, such as:

- validator instance
- default tasks
- in-memory repository
- temporary directory
- test database data
- setup and teardown logic

### Normal, Boundary, and Invalid Cases

Normal:

```text
COMPLETE 2 → COMPLETE
```

Boundary:

```text
COMPLETE 1 → COMPLETE
COMPLETE 0 → INVALID
```

`1` is the minimum valid positive ID, while `0` is the adjacent invalid value.

Invalid:

```text
COMPLETE -1 → INVALID
```

`COMPLETE 10` is a valid multi-digit case, but it is not the most representative boundary case.

### Testability Improvements

Useful directions include:

- separate responsibilities
- define clear inputs, outputs, and error contracts
- isolate I/O and other side effects
- avoid global mutable state
- inject dependencies
- test logic independently from formatting

Responsibility separation does not mean every method must be placed in a separate `.java` file.

### Stub and Mock

A stub provides predefined data or errors so the tested code enters a specific state.

A mock focuses on verifying interactions such as:

- whether a method was called
- arguments
- call count
- call order when relevant

Test doubles make unit tests faster and deterministic, but excessive mocking can couple tests to implementation details and create false confidence.

Real or isolated database integration tests are still necessary to validate schema, SQL, transactions, and driver behavior.

## Interview Review

Six topics were completed:

1. Unit, integration, and regression testing
2. Arrange–Act–Assert
3. Test case and test fixture
4. Normal, boundary, and invalid cases
5. Improving Java testability
6. Stub and mock

Review time:

```text
14:37:12～16:49:49
2 hours, 12 minutes, 37 seconds
```

This was much longer than the estimated 40–45 minutes because each answer required interpretation, example mapping, classification correction, and a revised interview response.

### Stronger Topics

- normal, boundary, and invalid cases
- improving Java code testability

### Topics Requiring More Practice

- distinguishing unit, integration, and regression tests
- Arrange–Act–Assert boundaries
- concise interview explanations
- precise time and space complexity wording

## Learner-led Reflection

### Most Valuable Learning

- separating validation logic, Demo output, and automated tests
- directly testing `TaskCommandValidator.identify()`
- understanding that regression describes a testing purpose rather than a testing level
- applying Arrange–Act–Assert
- understanding how fixed-size frequency arrays affect complexity

### Most Difficult or Time-consuming Parts

- diagnosing VS Code source roots across directories
- understanding interview questions and converting implementation details into concise explanations
- reviewing previous implementations while answering
- absorbing detailed feedback after each interview answer
- understanding implementation details that were broadly recognizable at first glance
- diagnosing the temporary GitHub push failure

### Source Root Learning

For an unmanaged Java project:

```text
Java: Configure Classpath
→ Add Source Root
→ Select Source Folder
→ Apply Settings
```

Both production and test roots must correspond to the directory above the `com/interview` package path.

### If Repeating Day 12

No major implementation change was requested.

The learner considered:

- the workload appropriate
- six interview questions appropriate
- the review depth appropriate

### Process Feedback

- Do not install Maven without a planned setup.
- If Maven is needed later, provide guided installation and verification.
- Continue improving precision when explaining time and space complexity.
- Preserve a separate learner-led Reflection before Daily Log creation.

### Optional Review

No optional review will be performed because Day 12 has a 19:15 stop requirement for the 19:30 event.

## Workload Observation

Day 12 is the third valid observation day.

The learner evaluated the Day 12 workload as:

```text
Appropriate
```

The final comparison will use:

- Day 9
- Day 11
- Day 12

No workload change will be made without the learner's final decision.

## Time Record

- Day 12 start: 2026-08-28 08:37:24 CST
- Short break: 08:56:07～09:08:44
- Validator and Test Commit／Push: 11:23:23
- Lunch: 11:24:05～13:36:24
- HackerRank completed: 14:28:51
- HackerRank Commit／Push: 14:35:41
- Interview Review: 14:37:12～16:49:49
- Reflection: 16:50:54～17:17:27

Effective training time before Daily Log and final delivery:

```text
6 hours, 15 minutes, 7 seconds
```

The final effective training time will be calculated after the pull request, local cleanup, workload comparison, and Issue closure are complete.