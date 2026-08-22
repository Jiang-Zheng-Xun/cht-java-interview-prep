# Day 6 — Java Interfaces and Polymorphism

Date: 2026-08-22
Issue: #11
Branch: `feature/day06-interfaces-polymorphism`

## Goals

- Understand Java interface contracts and implementations.
- Practice runtime polymorphism and dynamic dispatch.
- Introduce a repository abstraction for Task persistence.
- Complete the HackerRank Java Interface challenge.
- Keep testing proportional to the risk and size of the exercise.
- Reduce daily workload and checkpoint reporting overhead.

## Daily schedule

- Start time: 09:26:43 CST
- Lunch and rest: approximately 12:00–15:00 CST
- Dinner and rest: 18:26:16–19:52:07 CST
- Estimated effective training before closeout: approximately 6 hours
- Planned finish target: around 20:00 CST
- Actual final completion time: recorded after PR merge and cleanup

Because Day 6 started later than the reference 08:00 schedule, the
required workload was reduced instead of shortening meal and rest time.

## Git workflow

Day 6 started from a clean and synchronized `develop` branch:

```text
4eb0f3a Merge pull request #10 from
Jiang-Zheng-Xun/feature/day05-exceptions-file-io
```

Feature branch:

```text
feature/day06-interfaces-polymorphism
```

Feature commits:

```text
c95391a feat: add task repository abstraction
faefc93 feat: complete Java Interface challenge
```

## TaskRepository interface

Created:

```text
task-manager/src/com/interview/TaskRepository.java
```

The interface defines the persistence contract:

```java
public interface TaskRepository {
    void saveTasks(List<Task> tasks) throws IOException;

    List<Task> loadTasks() throws IOException;
}
```

`List<Task>` is used instead of `ArrayList<Task>` because `List`
is an interface type. The API depends on the List operation
contract instead of a particular implementation, reducing
coupling and allowing different List implementations.

## InMemoryTaskRepository

Created:

```text
task-manager/src/com/interview/InMemoryTaskRepository.java
```

It implements the repository contract with an in-memory List:

```java
public class InMemoryTaskRepository
        implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void saveTasks(List<Task> tasks) {
        Objects.requireNonNull(
                tasks,
                "tasks must not be null");

        List<Task> snapshot = List.copyOf(tasks);

        this.tasks.clear();
        this.tasks.addAll(snapshot);
    }

    @Override
    public List<Task> loadTasks() {
        return List.copyOf(tasks);
    }
}
```

The implementation does not declare `throws IOException`.
An overriding method may declare the same checked exception, a
more specific checked exception, or no checked exception. It
cannot broaden `IOException` to `Exception`.

This is better described as the overriding checked exception
narrowing rule.

## Polymorphism demo

Created:

```text
task-manager/src/com/interview/TaskRepositoryPolymorphismDemo.java
```

Key declaration:

```java
TaskRepository repository =
        new InMemoryTaskRepository();
```

- Declared type: `TaskRepository`
- Constructor call: `new InMemoryTaskRepository()`
- Runtime object type: `InMemoryTaskRepository`

The compiler uses the declared type to determine which methods
may be called. At runtime, dynamic dispatch selects the
overriding implementation belonging to the runtime object.

Observed output:

```text
Repository type: InMemoryTaskRepository
Source tasks after clear: 0
Loaded tasks: 2
1. [ ] Review interfaces
2. [x] Practice polymorphism
Loaded task list is unmodifiable.
```

Clearing the source List did not erase the repository data,
showing that the repository did not retain the caller's List
reference directly.

## Defensive copy and shallow copy

`List.copyOf(tasks)` returns an unmodifiable List snapshot.

It prevents structural changes such as:

```java
loadedTasks.add(task);
loadedTasks.remove(0);
loadedTasks.clear();
```

Those operations throw `UnsupportedOperationException`.

However, this remains a shallow copy. The snapshot contains the
same Task object references. If Task is mutable, an operation
such as this may still change the object:

```java
loadedTasks.get(0).markCompleted();
```

Therefore, `List.copyOf()` protects the List structure but does
not provide deep immutability for its elements.

## Interface and abstract class

An interface primarily defines an operation contract. Different
classes can implement the same contract without sharing a class
inheritance chain.

An abstract class can provide:

- Instance fields
- Constructors
- Abstract methods
- Shared concrete method implementations

An abstract class is appropriate when subclasses have a valid
is-a relationship and need shared state or implementation.

A Java class can extend only one class but can implement multiple
interfaces. `TaskRepository` only needs a persistence contract,
so an interface is more appropriate than an abstract class.

## HackerRank — Java Interface

Source:

```text
hacker-rank/day06/JavaInterface.java
```

Challenge:

```text
https://www.hackerrank.com/challenges/java-interface/problem
```

The program declares:

```java
interface AdvancedArithmetic {
    int divisor_sum(int n);
}
```

`MyCalculator` implements the interface and calculates the sum
of all positive divisors.

Important HackerRank editor finding:

The Run Code skeleton used during this practice only contained
imports and an empty `Solution.main()`. It did not provide the
`AdvancedArithmetic` interface. Therefore, the interface,
`MyCalculator`, input handling, and output handling all had to
be completed.

The actual editor content must be checked instead of relying
only on an ambiguous reading of the problem statement.

### Local tests

| Input | Expected divisor sum | Result |
|---:|---:|---|
| `6` | `12` | PASS |
| `1` | `1` | PASS |
| `1000` | `2340` | PASS |

HackerRank Run Code and Submit Code both passed.

No fixture files or batch script were created because the
program has a small input surface and simple deterministic
logic. The official sample and two high-value boundaries were
sufficient for this exercise.

## Divisor-sum complexity

The implemented version examines all candidates from `1`
through `n`:

```text
Time: O(n)
Extra space: O(1)
```

A square-root version can process divisor pairs. If `candidate`
divides `n`, its paired divisor is:

```java
int pairedDivisor = n / candidate;
```

Only candidates satisfying the following need to be checked:

```java
candidate * candidate <= n
```

For a perfect square, the square root and paired divisor are the
same and must only be added once.

The optimized complexity would be:

```text
Time: O(sqrt(n))
Extra space: O(1)
```

## Interview review

Six focused questions were completed:

1. Interface contracts and implementation coupling
2. Runtime polymorphism and dynamic dispatch
3. Overriding checked exception narrowing
4. Unmodifiable List and shallow copy
5. Interface versus abstract class
6. Divisor-sum complexity and square-root optimization

## Repeated mistakes and corrections

- `List` is an interface contract, not an unfinished contract.
- `ArrayList` is one implementation of `List`.
- Runtime polymorphism is not merely the reuse of a method name.
- Overloading is commonly described as compile-time
  polymorphism; overriding with dynamic dispatch is runtime
  polymorphism.
- The compiler checks callable methods from the declared type;
  the constructor creates the runtime object.
- `List.copyOf()` protects the List structure, not mutable
  element state.
- Abstract classes are used for shared state or implementation,
  not simply because stronger coupling is desired.
- The checked exception rule is more clearly described as
  narrowing rather than only as exception covariance.
- A square-root divisor algorithm must avoid adding the square
  root twice for a perfect square.
- HackerRank requirements must be confirmed against the actual
  editor skeleton.

## Useful commands

Compile and run the repository demo:

```bash
rm -rf task-manager/out
mkdir -p task-manager/out

javac \
    -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/TaskRepository.java \
    task-manager/src/com/interview/InMemoryTaskRepository.java \
    task-manager/src/com/interview/TaskRepositoryPolymorphismDemo.java

java \
    -cp task-manager/out \
    com.interview.TaskRepositoryPolymorphismDemo
```

Compile and test Java Interface:

```bash
rm -rf hacker-rank/day06/out
mkdir -p hacker-rank/day06/out

javac \
    -d hacker-rank/day06/out \
    hacker-rank/day06/JavaInterface.java

for number in 6 1 1000; do
    printf '%s\n' "$number" \
        | java \
            -cp hacker-rank/day06/out \
            JavaInterface \
        | tail -n 1
done
```

Expected final lines:

```text
12
1
2340
```

## Reflection

The interface concept became clearer by treating it like a
product specification: implementations must follow its
operation contract.

The main areas still requiring practice are:

- Explaining polymorphism without reducing it to method names
- Choosing between an interface and an abstract class
- Recalling square-root divisor-pair optimization
- Using precise declared-type and runtime-object terminology

The lighter testing policy was sufficient for this exercise and
provided a better balance than creating many low-value fixtures.

The shorter checkpoint forms, one core interview question at a
time, and repeated exact commands reduced the Day 5 reporting
overhead. This format will continue on Day 7.

Day 7 will continue with:

- One HackerRank exercise
- Approximately six focused interview questions
- Immediate concept checks after implementation
- Testing depth selected according to program risk
- Full Git synchronization and quality checks