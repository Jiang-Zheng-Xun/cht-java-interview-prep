# Day 5 — Java Exceptions and File Persistence

Date: 2026-08-21

## Goals

- Practice Java exception handling.
- Distinguish checked and unchecked exceptions.
- Practice `throw`, `throws`, catch ordering, and
  try-with-resources.
- Add text-file persistence to the Task Manager.
- Test normal, missing-file, and malformed-data paths.
- Complete one exception-related HackerRank challenge.
- Evaluate workload reduction and testing cost.

## Completed Work

- Added `ExceptionHandlingDemo`.
- Added `TaskFileRepository`.
- Added `TaskFilePersistenceDemo`.
- Added one malformed-data fixture.
- Added `JavaExceptionHandlingTryCatch`.
- Added 20 HackerRank input／expected-output fixtures.
- Added a batch script that runs 10 test cases.
- Passed HackerRank Run Code and all hidden tests.
- Completed six focused interview questions.

## Feature Commits

```text
1dfaec8 feat: add exception handling demo
704dd7e feat: add task file persistence
38a38a4 feat: complete Java Exception Handling challenge
```

## Exception Handling Demo

The demo tested four paths:

```text
No argument:
Usage: provide one task number.

Valid input:
Valid task number: 2
Validation finished.

Non-integer:
Task number must be an integer.
Validation finished.

Non-positive integer:
Task number must be positive.
Validation finished.
```

A command-line argument count of zero is different from explicitly
passing an empty or whitespace String.

## Checked and Unchecked Exceptions

### Checked exception

`IOException` is checked because it extends `Exception` but not
`RuntimeException`.

The compiler requires the caller to either:

- Catch it with `try-catch`.
- Continue declaring it with `throws`.

### Unchecked exception

`IllegalArgumentException` extends `RuntimeException`, so it is
unchecked.

Unchecked exceptions can still be caught and documented, but the
compiler does not require callers to catch or declare them.

## `throw` and `throws`

- `throw` appears inside executable code and throws an exception
  object.
- `throws` appears in a method signature and declares that the
  method may pass an exception to its caller.
- Declaring `throws IOException` does not mean every execution
  will fail.

## Catch Ordering

Catch ordering follows exception inheritance, not business
validation order.

```text
RuntimeException
└── IllegalArgumentException
    └── NumberFormatException
```

A specific subclass catch must appear before a compatible superclass
catch. Otherwise, the subclass catch is unreachable.

Sibling exception classes without an inheritance relationship do not
have this reachability restriction.

## `finally`

When an exception occurs:

1. The remaining statements in the try block are skipped.
2. Java searches for a matching catch block.
3. The finally block normally executes.
4. If the exception was handled, execution can continue afterward.
5. If it was not handled, it propagates after finally.

A return before entering the try block does not execute that try
statement's finally block.

## Try-with-resources

`TaskFileRepository` uses try-with-resources:

```java
try (BufferedReader reader =
        Files.newBufferedReader(...)) {
    // Read data.
}
```

Resources implementing `AutoCloseable` or `Closeable` are closed
automatically on normal and exceptional paths.

This reduces duplicated cleanup code and the risk of resource leaks.

## Task File Persistence

Responsibilities:

- `Task`: one task's title and completion state.
- `TaskManager`: in-memory business operations.
- `TaskFileRepository`: persistence operations.
- `TaskFilePersistenceDemo`: application flow and user-facing error
  messages.

Text-file format:

```text
completed<TAB>title
```

Example:

```text
false^IReview exceptions
true^IWrite file tests
```

Tab and newline characters are rejected in titles because they would
break the current line-based format.

## Save and Load Results

Normal result:

```text
Saved tasks: 2
Loaded tasks: 2
1. [ ] Review exceptions
2. [x] Write file tests
```

The loaded completed state is restored through:

```java
task.markCompleted();
```

This preserves Task encapsulation instead of directly modifying its
private field.

## Failure Policies

### Missing file

```text
Task file not found:
task-manager/out/day05-file-that-does-not-exist.txt
```

`NoSuchFileException` is caught before the more general
`IOException`.

### Malformed data

```text
Task file error: Malformed task data at line 1.
```

The repository uses a fail-fast policy. It rejects malformed data
instead of silently returning a partially loaded list.

Fail-fast does not automatically guarantee security or prevent all
crashes. Its main purpose here is to prevent silent data loss and
inconsistent partial state.

## Exception Translation and Chaining

When a file title violates the Task constructor invariant, the
repository converts the unchecked `IllegalArgumentException` into an
`IOException` containing file and line context.

The original exception is preserved as the cause:

```java
throw new IOException(
        "Malformed task data at line "
                + lineNumber
                + ".",
        exception);
```

It can later be inspected with `getCause()` and appears as
`Caused by` in a stack trace.

`IOException` is not a superclass of `IllegalArgumentException`;
the repository intentionally creates a new exception and chains the
original cause.

## Repository Responsibility

`TaskFileRepository` does not directly print errors because it does
not know whether its caller is a CLI, GUI, Web API, or background
service.

It reports failure through exceptions. The application layer decides
whether to display a message, retry, log, or terminate the operation.

Separating persistence from business operations makes it easier to
add a database implementation later without changing Task's domain
model.

## HackerRank — Java Exception Handling

Challenge:

```text
Java Exception Handling (Try-catch)
```

The source documentation includes:

- Complete Problem, Input, and Output requirements.
- All four official samples.
- Java `int` range.
- Important edge cases.
- Six additional local test cases.
- Complexity analysis.
- Runtime-dependent exception output considerations.

Complexity:

```text
Time: O(1)
Extra space: O(1)
```

## Automated Tests

The test set contains:

- Four official sample cases.
- Negative dividend.
- Negative divisor.
- Two negative values.
- Zero dividend.
- Invalid second value.
- Out-of-range integer.

Final result:

```text
Result: 10 passed, 0 failed
```

The script compiles once, automatically pairs input and expected
files, writes actual output under ignored `out/`, displays unified
diff on failure, and returns a non-zero exit code if any test fails.

## Hidden Test Failure and Fix

The first local JDK 21 result for out-of-range input included:

```text
java.util.InputMismatchException:
For input string: "2147483648"
```

HackerRank expected only:

```text
java.util.InputMismatchException
```

The difference was not caused by JIT optimization. Exception messages
can vary with Java runtime and library implementation.

Final implementation:

```java
System.out.println(
        exception.getClass().getName());
```

This prints a stable fully qualified class name for
`InputMismatchException`.

`ArithmeticException` still prints the exception object because the
required output includes:

```text
/ by zero
```

The corrected submission passed all hidden tests.

## Exception Output Methods

- `getClass().getName()`: fully qualified exception class name.
- `getMessage()`: message only; may be `null`.
- `toString()`: class name plus message when a message exists.
- `println(exception)`: invokes `toString()`.
- `printStackTrace()`: prints the full stack trace and is not suitable
  when the required output is only one line.

## Interview Review — Easy Mistakes

- Handling a checked exception means catch it or continue declaring
  `throws`; it does not mean creating another exception.
- Catch ordering depends on inheritance.
- `IOException` and `IllegalArgumentException` do not have a
  superclass-subclass relationship.
- The source of an error does not determine whether its exception
  class is checked or unchecked.
- Exception chaining preserves the original cause.
- Repository code should not decide presentation behavior.
- Responsibility separation does not automatically provide database
  security, transactions, or auditing.
- Runtime-specific exception messages should not be treated as a
  stable output contract.

## Testing Decision

The full 10-case batch test was valuable because it:

- Detected a genuine expected-output mismatch.
- Displayed the exact diff and file paths.
- Returned exit code `1` on failure.
- Preserved the out-of-range case as a regression test.

However, manually creating 20 fixtures required significant time.

Future default:

- Official samples.
- Two high-value boundary cases.
- Full batch testing only for multi-branch, algorithmic, or
  higher-risk exercises.
- Compare learning value against setup and maintenance cost.

## Reflection

### Objective evidence

- Four exception-demo paths passed.
- Normal persistence saved and restored two Tasks.
- Missing-file handling passed.
- Malformed-data fail-fast handling passed.
- The batch script detected a real failure.
- Ten local tests passed after correction.
- HackerRank passed all hidden tests.
- Three atomic feature commits were pushed.
- Six interview questions were completed.

### Learning gaps

- Exception inheritance is not yet automatic.
- Checked／unchecked classification needs more examples.
- Exception chaining is still unfamiliar.
- Business and persistence responsibility descriptions need more
  practice.
- Exception output contracts must be distinguished from
  runtime-specific messages.

### Loading

Start time:

```text
08:42 CST
```

Recorded active periods:

```text
08:42–13:45
16:00–18:00
19:20–22:07
```

Approximate active training time:

```text
9 hours 50 minutes
```

This exceeded the preferred eight-hour workload by approximately one
hour and fifty minutes. At 22:07, delivery and cleanup were still not
finished.

### Day 6 Adjustments

- Limit total active training, including delivery, to about eight
  hours.
- Keep about six interview questions, with one core question each.
- Stop expanding one question into many subquestions.
- Use official samples plus two important boundaries by default.
- Stop adding major implementation work after approximately 7:30 PM.
- Reserve time for Reflection, Daily Log, Git final checks, PR, and
  cleanup.
- Preserve roughly 1–1.5 hours around noon.
- Preserve at least one hour for dinner.
- Leave evening time for English practice and normal rest.
- Every stage must include its goal, rationale, API hints, paths,
  commands, expected result, and checkpoint.