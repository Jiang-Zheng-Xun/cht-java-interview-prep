# Day 10 — Java Date and Time API

Date: 2026-08-26

## Learning Goals

* Use `LocalDate` to create, parse, and compare dates.
* Understand the immutable nature of Java date objects.
* Distinguish `parse()` from `format()`.
* Calculate the number of days between dates.
* Handle invalid date input.
* Complete a date-processing demo and HackerRank challenge.

## Project Management

### Daily Start

* Started at: 2026-08-26 08:57:07 CST
* Repository: `/home/logos/projects/cht-java-interview-prep`
* Starting branch: `develop`
* Working tree: clean
* `origin/develop...HEAD`: `0 0`
* Latest `develop` commit:

```text
4485286 Merge pull request #18 from
Jiang-Zheng-Xun/feature/day09-lambdas-streams
```

No pull was required because local `develop` and `origin/develop`
were synchronized.

Approximately 20 minutes during the Daily Start stage were spent
showering and were excluded from effective training time.

### Issue and Feature Branch

* Issue: #19
* Issue title: `Day 10: Practice Java Date and Time API`
* Feature branch: `feature/day10-date-time`
* Branch starting commit: `4485286`
* Upstream tracking was configured successfully.

### Feature Commits

```text
ae2578e feat: add task deadline demo
68054ea feat: complete Java Date and Time challenge
```

## Task Deadline Demo

Created:

```text
task-manager/src/com/interview/TaskDeadlineDemo.java
```

The demo used a fixed date:

```java
LocalDate today =
        LocalDate.of(2026, 8, 26);
```

A fixed date was used instead of `LocalDate.now()` so regression
tests remain deterministic and produce the same output on different
days.

### Date Parsing

```java
private static LocalDate parseDeadline(
        String deadlineText) {
    return LocalDate.parse(
            deadlineText,
            DATE_FORMATTER);
}
```

`LocalDate.parse()` converts a `String` into a `LocalDate`.

An invalid date such as:

```text
2026-02-30
```

cannot be represented by `LocalDate`, so parsing throws a
`DateTimeParseException`.

The demo catches that exception and produces a clear result instead
of terminating the complete process.

### Date Classification

The demo classifies each deadline as:

* Overdue
* Due today
* Upcoming
* Invalid date

Date comparison uses:

```java
deadline.isBefore(today)
deadline.isEqual(today)
deadline.isAfter(today)
```

Days between dates are calculated using:

```java
ChronoUnit.DAYS.between(start, end)
```

The direction matters:

* Overdue: calculate from `deadline` to `today`
* Upcoming: calculate from `today` to `deadline`

### Actual Output

```text
Today: 2026-08-26
Review Date API: Overdue by 1 day
Write daily log: Due today
Practice HackerRank: Upcoming in 3 days
Check invalid date: Invalid date: 2026-02-30
```

### Deterministic Testing

Using:

```java
LocalDate.now()
```

would make the result depend on the system date. A test could pass
on 2026-08-26 but fail after midnight or when executed on another
day.

Using a fixed `LocalDate` makes the expected output repeatable.

### Task Deadline Demo Commit

```text
ae2578e feat: add task deadline demo
```

The commit was pushed successfully, and the working tree was clean.

## HackerRank — Java Date and Time

Created:

```text
hacker-rank/day10/JavaDateAndTime.java
```

Challenge:

```text
https://www.hackerrank.com/challenges/java-date-and-time/problem
```

The challenge receives input in this order:

```text
month day year
```

The `LocalDate.of()` API contract requires:

```java
LocalDate.of(year, month, day)
```

Arguments must therefore be reordered according to the public API
signature.

### Implementation

```java
public static String findDay(
        int month,
        int day,
        int year) {
    LocalDate date =
            LocalDate.of(year, month, day);

    return date.getDayOfWeek().toString();
}
```

`DayOfWeek.toString()` produces the uppercase English value required
by HackerRank.

### Local Tests

Official sample:

```text
Input:
08 05 2015

Output:
WEDNESDAY
```

Leap-year case:

```text
Input:
02 29 2024

Output:
THURSDAY
```

Year-boundary case:

```text
Input:
01 01 2000

Output:
SATURDAY
```

All three local tests passed.

No fixture files or test script were created because the three
single-line cases were small and easy to verify directly.

### Actual HackerRank Editor Skeleton

The editor was not a minimal empty `Solution` class.

It provided:

* class `Result`
* the `findDay(month, day, year)` method signature
* class `Solution`
* input parsing
* output handling

The requested implementation area was the body of:

```java
Result.findDay(month, day, year)
```

This contradicted the previous assumption that HackerRank editor
skeletons were always identical.

Future HackerRank exercises must first inspect the actual editor
skeleton and determine whether it provides:

* only a minimal `Solution` class, or
* a challenge-specific structure and locked method area.

The local version remains a standalone executable class for
repeatable compilation and testing.

### HackerRank Result

* Run Code: passed
* Official sample: passed
* Submit Code: passed all tests
* Hidden tests: passed
* Local public class remained `JavaDateAndTime`
* No compilation errors occurred

### HackerRank Commit

```text
68054ea feat: complete Java Date and Time challenge
```

The commit was pushed successfully, and the working tree was clean.

## Interview Review

### 1. Why Prefer `java.time`?

Compared with `java.util.Date` and `Calendar`, the `java.time` API
has clearer responsibilities and more expressive operations.

For example:

* `LocalDate` represents a date without time or time zone.
* `LocalDateTime` represents a date and time without time zone.
* `ZonedDateTime` represents a date and time with a time zone.

Most core `java.time` classes are immutable, making them safer and
more suitable for concurrent use.

Date arithmetic, comparison, parsing, and formatting are also more
direct than the older APIs.

### 2. `LocalDate` Immutability

```java
LocalDate original =
        LocalDate.of(2026, 8, 26);

LocalDate result =
        original.plusDays(1);
```

Results:

```text
original: 2026-08-26
result:   2026-08-27
```

`plusDays()` does not modify the original object. It returns another
`LocalDate`.

If the code uses:

```java
original = original.plusDays(1);
```

the old object is still not modified. The assignment stores the new
reference value in the `original` reference variable.

This distinguishes:

* changing an object's internal state
* changing which object a reference variable points to

### 3. Pass-by-Value Is Not Immutability

The original primitive variables:

```java
int year;
int month;
int day;
```

are not modified by `LocalDate.of()` because Java passes copies of
their primitive values to the method.

That explains why the caller's variables do not change.

`LocalDate` immutability is a separate class-design property:
operations do not change an existing `LocalDate`; they return a new
object.

### 4. `parse()` and `format()`

```text
parse():  String → LocalDate
format(): LocalDate → String
```

Example:

```java
LocalDate date =
        LocalDate.parse("2026-08-26");
```

```java
String text =
        date.format(
                DateTimeFormatter.ISO_LOCAL_DATE);
```

Parsing invalid text or an impossible date may throw
`DateTimeParseException`.

### 5. `Period` and `Duration`

* `Period` represents date-based amounts using years, months, and
  days.
* `Duration` represents time-based amounts using seconds and
  nanoseconds.

Examples:

```text
2 years, 3 months, 5 days → Period
90 minutes                 → Duration
```

### 6. `DateTimeParseException`

`DateTimeParseException` is an unchecked exception because it
inherits from `RuntimeException`.

The compiler does not require `catch` or `throws` for unchecked
exceptions.

If date input comes from a user, file, or external API, the program
should normally catch the exception at an appropriate boundary and
return a clear error.

If an invalid date is hard-coded inside the program, it may instead
represent a programming defect and should not simply be ignored.

## Repeated Mistakes and Weak Concepts

### Pass-by-Value and Immutability

These concepts were mixed together more than once.

Correct distinction:

* Pass-by-value explains how an argument value is copied into a
  method parameter.
* Immutability explains why an existing object's state cannot be
  changed after construction.

### Object State and Reference Reassignment

This statement:

```java
original = original.plusDays(1);
```

does not modify the old `LocalDate`.

It creates or returns another object and stores its reference value
in the `original` variable.

### Public API Versus Internal Implementation

The argument order for `LocalDate.of()` should be explained through
its public method signature:

```java
LocalDate.of(int year, int month, int dayOfMonth)
```

It should not be justified through the private constructor or other
internal implementation details.

### Terminology and Spelling

Use:

```text
LocalDate
plusDays()
time zone
DateTimeParseException
```

Avoid:

```text
LocalDay
pulsDay()
region when time zone is intended
```

## Common Commands

```bash
# Task Deadline Demo
javac \
    -d task-manager/out \
    task-manager/src/com/interview/TaskDeadlineDemo.java

java \
    -cp task-manager/out \
    com.interview.TaskDeadlineDemo

# Java Date and Time
javac \
    -d hacker-rank/day10/out \
    hacker-rank/day10/JavaDateAndTime.java

java \
    -cp hacker-rank/day10/out \
    JavaDateAndTime <<'EOF'
08 05 2015
EOF

# Git quality checks
git status --short
git diff --check
git diff --cached --check
git rev-list --left-right --count origin/develop...HEAD
git diff --name-status develop...HEAD
```

## Process Improvements

### Important Stage Timestamps Only

Recording time after every small substep created unnecessary
overhead.

Future days will record only:

* daily start
* main implementation Commit and Push
* HackerRank Commit and Push
* lunch, dinner, or long breaks
* Interview Review completion
* optional review start and stop
* Reflection completion
* final Merge and Cleanup

### HackerRank Skeleton Inspection

Every HackerRank challenge must inspect its actual editor skeleton
before implementation.

Do not assume that the structure matches the previous challenge.

### Optional Short Review Stage

When effective training time is substantially below seven hours,
the assistant may offer a short review stage.

Rules:

1. Ask for explicit permission before starting.
2. Record the review start time.
3. Ask only one short question at a time.
4. After reviewing each answer, ask whether to continue.
5. Use previous implementations, repeated mistakes, and core
   concepts as the question source.
6. Use the answers to identify weak areas and improve the final
   interview question set.
7. The learner may send a time after approximately 19:30 as a stop
   signal.
8. Review time counts as effective training time.

No optional review was started on Day 10 because training resumed
late and the learner was already fatigued.

## Reflection

### Concepts That Became Clearer

* `LocalDate`
* date parsing
* date comparison
* deterministic date testing
* HackerRank date calculation

### Concepts Requiring More Practice

* pass-by-value versus immutability
* object state versus reference reassignment
* `Period` versus `Duration`
* the Java date/time class hierarchy

### Workload Evaluation

The content workload could not be evaluated reliably because the
learner spent a long period outside handling family matters and
returned already fatigued.

The late completion time must not be interpreted as evidence that
the planned workload was excessive.

Day 10 is excluded from the three-day workload observation sample.

Updated observation days:

* Day 9: observation day 1
* Day 10: excluded
* Day 11: planned observation day 2
* Day 12: planned observation day 3

No workload change will be made until the valid observation days are
complete and the learner makes the final decision.

## Time Record

* Day 10 start: 2026-08-26 08:57:07 CST
* Daily Start Check completed: 09:47:44 CST
* Shower during Daily Start: approximately 20 minutes
* Issue Setup completed: 09:50:32 CST
* Feature Branch Setup completed: 09:56:41 CST
* Task Deadline Demo committed and pushed: 11:09:17 CST
* Lunch and family errand started: 11:09:17 CST
* Training resumed: 19:55:55 CST
* HackerRank committed and pushed: 20:51:55 CST
* Interview Review completed: 21:33:13 CST
* Reflection completed: 21:48:08 CST

Current effective training time before Daily Log and final delivery:

```text
3 hours, 44 minutes, 23 seconds
```

The final effective training time will be calculated after the pull
request, local cleanup, and Issue closure are complete.
