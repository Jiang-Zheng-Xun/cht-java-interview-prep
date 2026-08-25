# Day 9 — Java Lambda Expressions and Stream API

Date: 2026-08-25

## Training Goals

* Understand the relationship between lambda expressions and functional interfaces.
* Practice a Stream pipeline using `filter()`, `sorted()`, `map()`, and `toList()`.
* Complete the HackerRank Java Lambda Expressions challenge.
* Review common functional interfaces and prime-number checking.
* Restore the lighter Day 7 workload rhythm after the excessive Day 8 workload.

## Project Management

### Daily Start

* Started at: 2026-08-25 08:36:46 CST
* Repository: `/home/logos/projects/cht-java-interview-prep`
* Starting branch: `develop`
* Working tree: clean
* `origin/develop...HEAD`: `0 0`
* Latest `develop` commit:

```text
ae6dd21 Merge pull request #16 from Jiang-Zheng-Xun/feature/day08-collections-sorting
```

No pull was required because local `develop` and `origin/develop` were synchronized.

### Issue and Feature Branch

* Issue: #17
* Issue title: `Day 9: Practice Java lambda expressions and Stream API`
* Feature branch: `feature/day09-lambdas-streams`
* Branch starting commit: `ae6dd21`
* Upstream tracking was configured successfully.
* The working tree remained clean after branch setup.

### Feature Commits

```text
a8bcb2a feat: add task stream demo
da4fc93 feat: complete Java Lambda Expressions challenge
```

## Task Stream Demo

Created:

```text
task-manager/src/com/interview/TaskStreamDemo.java
```

The demo used a Stream pipeline to select incomplete tasks, order them by title, transform them into task titles, and collect the result into a list.

Core pipeline:

```java
List<String> incompleteTitles =
        tasks.stream()
                .filter(task -> !task.isCompleted())
                .sorted(
                        Comparator.comparing(
                                Task::getTitle))
                .map(Task::getTitle)
                .toList();
```

Actual result:

```text
Incomplete titles: [Review lambdas, Write stream demo]
```

The source task order remained unchanged before and after the Stream pipeline.

### Stream Pipeline Analysis

1. `filter(task -> !task.isCompleted())` keeps only tasks whose completed state is `false`.
2. `sorted(Comparator.comparing(Task::getTitle))` orders the remaining elements by task title.
3. `map(Task::getTitle)` transforms `Stream<Task>` into `Stream<String>`.
4. `toList()` materializes the result as a `List<String>`.

Intermediate operations:

* `filter()`
* `sorted()`
* `map()`

Terminal operation:

* `toList()`

Intermediate Stream operations are lazy. They describe the processing pipeline but do not produce the complete result until a terminal operation is invoked.

A Stream is not a data structure and does not permanently store elements. It is a single-use processing pipeline over a data source.

Standard operations such as `filter()`, `sorted()`, and `map()` do not modify the source collection by themselves. However, a lambda containing side effects could still modify external objects or state.

### Task Stream Demo Commit

```text
a8bcb2a feat: add task stream demo
```

The commit was pushed successfully, and the working tree was clean.

## HackerRank — Java Lambda Expressions

Created:

```text
hacker-rank/day09/JavaLambdaExpressions.java
```

Challenge:

```text
https://www.hackerrank.com/challenges/java-lambda-expressions/problem
```

The HackerRank editor provided only a minimal `Solution` skeleton. It did not provide:

* `PerformOperation`
* `MyMath`
* `checker()`
* input parsing
* output handling

Therefore, the complete solution structure had to be implemented.

The local source kept:

```java
public class JavaLambdaExpressions
```

Only the HackerRank submission changed the public class name to:

```java
public class Solution
```

### Functional Interface

```java
@FunctionalInterface
interface PerformOperation {
    boolean check(int number);
}
```

A functional interface has exactly one abstract method.

The `@FunctionalInterface` annotation is optional, but it allows the compiler to verify that the interface continues to satisfy this rule.

### Lambda Implementations

Odd-number check:

```java
PerformOperation isOdd() {
    return number -> number % 2 != 0;
}
```

Prime-number check:

```java
PerformOperation isPrime() {
    return number -> {
        if (number < 2) {
            return false;
        }

        for (int divisor = 2;
                divisor <= number / divisor;
                divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    };
}
```

Palindrome check:

```java
PerformOperation isPalindrome() {
    return number -> {
        String text = Integer.toString(number);
        String reversed =
                new StringBuilder(text)
                        .reverse()
                        .toString();

        return text.equals(reversed);
    };
}
```

### Lambda Invocation Flow

The main method selects an implementation:

```java
operation = myMath.isOdd();
```

or:

```java
operation = myMath.isPrime();
```

or:

```java
operation = myMath.isPalindrome();
```

It then passes the selected behavior and input number to:

```java
MyMath.checker(operation, number);
```

The checker invokes:

```java
return operation.check(number);
```

`PerformOperation.check(int)` defines an operation contract whose return type is `boolean`.

The `operation` reference points to the selected lambda implementation. Calling `operation.check(number)` executes that implementation and returns its result.

The main method selects the operation. The checker does not decide whether the odd, prime, or palindrome implementation should run.

### Local Test Results

Official sample:

```text
EVEN
PRIME
PALINDROME
ODD
COMPOSITE
```

Additional local cases:

```text
ODD
EVEN
COMPOSITE
PRIME
COMPOSITE
PRIME
PALINDROME
NOT PALINDROME
```

Both local test groups passed.

No fixture script was created because the direct input cases were small and sufficiently clear. Automated fixtures should be selected according to problem scale and risk instead of being added mechanically to every exercise.

### HackerRank Result

* Run Code: passed
* Submit Code: passed all tests
* Hidden tests: passed
* Local public class remained `JavaLambdaExpressions`
* No compilation errors occurred

### HackerRank Commit

```text
da4fc93 feat: complete Java Lambda Expressions challenge
```

The commit was pushed successfully, and the working tree was clean.

## Prime-Number Algorithm

A composite positive integer has factors that occur in pairs:

```text
divisor1 * divisor2 = number
```

If `number` is composite, at least one factor must be less than or equal to `sqrt(number)`. Therefore, checking possible divisors only through the square-root boundary is sufficient.

The loop uses:

```java
divisor <= number / divisor
```

instead of:

```java
divisor * divisor <= number
```

The division form avoids a possible integer overflow caused by multiplying two large `int` values.

Complexity:

* Time: `O(sqrt(N))`
* Extra space: `O(1)`

## Interview Review

### 1. Lambda and Functional Interface

A lambda expression has one of the following general forms:

```java
(parameters) -> expression
```

or:

```java
(parameters) -> {
    statements
}
```

A lambda can provide an implementation of the single abstract method defined by a functional interface.

A lambda and an anonymous class can both implement `PerformOperation.check(int)`. A lambda is normally preferred in this case because it removes anonymous-class and method-override boilerplate.

It is not precise to claim that a lambda never creates an object or is always reused. Lambda allocation and caching are JVM implementation details.

### 2. Common Functional Interfaces

| Required operation | Functional interface     |
| ------------------ | ------------------------ |
| `Task -> boolean`  | `Predicate<Task>`        |
| `Task -> String`   | `Function<Task, String>` |
| `Task -> void`     | `Consumer<Task>`         |
| `() -> Task`       | `Supplier<Task>`         |

`Consumer<T>` and `Supplier<T>` remain concepts that require additional review.

### 3. Collection and Stream

The Java Collections Framework includes collection interfaces, implementations, algorithms, and `Map`-related types.

`List`, `Set`, and `Queue` extend the `Collection` interface. `Map` belongs to the Collections Framework but does not extend `Collection`.

A collection stores elements. A Stream describes a pipeline that processes elements from a source.

### 4. Intermediate and Terminal Operations

Intermediate operations return another Stream and are normally lazy.

Examples:

* `filter()`
* `sorted()`
* `map()`

A terminal operation starts pipeline evaluation and produces a result or side effect.

Examples:

* `toList()`
* `collect()`
* `forEach()`
* `count()`

### 5. Lambda Behavior Selection

Methods such as `isOdd()` return an implementation of `PerformOperation`; they do not immediately return the final boolean result.

The returned behavior can be stored in a variable, passed as a method argument, and executed later through `check(number)`.

### 6. Safe Prime Checking

Checking divisors only through the square-root boundary reduces the algorithm to `O(sqrt(N))`.

Using division in the loop condition prevents multiplication overflow.

## Repeated Mistakes and Weak Areas

The following areas need continued review:

1. Anonymous classes are still unfamiliar.
2. `Consumer<T>` and `Supplier<T>` are easy to confuse.
3. The relationships inside the Java Collections Framework are not yet sufficiently familiar.
4. `Map` must not be described as extending `Collection`.
5. Stream intermediate operations should not be described as immediately producing complete new collections.
6. Stream sorting should not be explained using a guaranteed sorting implementation such as TimSort because that is an implementation detail rather than the Stream API contract.
7. Lambda allocation and reuse should not be stated as guaranteed behavior.
8. The main method selects the lambda implementation; `checker()` only invokes the selected operation.

## Reflection

### Concepts That Became Clearer

The following concepts became clearer:

* the basic structure of a lambda expression
* the relationship between lambdas and functional interfaces
* behavior being passed through a functional-interface reference
* how `operation.check(number)` invokes the selected implementation
* the processing stages of a Stream pipeline
* the distinction between intermediate and terminal operations
* the square-root prime-checking algorithm
* avoiding integer overflow in a loop condition

### Concepts Requiring More Practice

The following concepts remain weaker:

* anonymous classes
* `Consumer<T>`
* `Supplier<T>`
* the Java Collections Framework hierarchy
* precise terminology for lazy Stream processing

### Workload Evaluation

The Day 9 workload was noticeably lighter than Day 8.

Using one main demo, one HackerRank challenge, and six focused interview topics provided more time to think about how the code worked.

Day 8 represented an excessive increase in workload. Day 9 restored the preferred Day 7 rhythm.

Day 10 will maintain the current workload. No training increase will be made without the learner's explicit decision.

### Three-Day Observation Rule

The workload observation period starts on Day 9:

* Day 9: observation day 1
* Day 10: observation day 2
* Day 11: observation day 3

If all three days contain less than seven hours of effective training, the recorded data will be presented before discussing an adjustment.

An initial possible adjustment is:

```text
review allowance = 7 hours - actual effective training time
```

This allowance would only be considered when:

* effective training time is substantially below the target,
* the actual clock time has not passed 20:00,
* the learner agrees to add review work.

Possible review work would consist of short randomized questions about previous implementations, repeated mistakes, and core concepts. Its purpose would be to identify weak areas and build a targeted interview question set, not simply increase workload.

### Process Improvement

Each major stage should record its completion time with:

```bash
date '+%Y-%m-%d %H:%M:%S %Z'
```

Suggested checkpoints include:

* Daily start and branch setup
* Main implementation
* HackerRank practice
* Interview review
* Reflection
* Daily Log
* Final branch checks
* Pull request and cleanup

This allows the training process to:

* measure the time spent on each stage,
* identify unusually expensive stages,
* recommend breaks at appropriate points,
* calculate effective training time more accurately,
* support evidence-based workload decisions.

## Time Record

* Day 9 start: 2026-08-25 08:36:46 CST
* Lunch break start: 2026-08-25 11:23:04 CST
* Training resumed: 2026-08-25 15:26:58 CST
* Interview Review completed: 2026-08-25 16:27:09 CST
* Reflection completed: 2026-08-25 17:01:06 CST

The final completion time and total effective training time will be recorded after the pull request, local cleanup, and Issue closure are complete.
