# Day 4 — Java OOP and Automated Output Testing

Date: 2026-08-20

## Goals

- Practice Java classes, objects, constructors, and instance fields.
- Understand encapsulation, access modifiers, and class invariants.
- Separate a task data model from task management operations.
- Practice inheritance, method overriding, `this`, and `super`.
- Create reusable expected-output fixtures.
- Use `diff` and exit codes to automate output verification.
- Create a shell script that runs multiple output tests.
- Complete two OOP-related HackerRank challenges.
- Review OOP concepts through interview questions.

## Git Workflow

### Daily start

- Started from a clean `develop` branch.
- Confirmed local `develop` matched `origin/develop`.
- Created Issue #7:
  `Day 4: Practice Java OOP and automated output testing`
- Created and pushed:
  `feature/day04-oop-testing`

### Feature commits

```text
f61cc2f feat: add Task object model
03a4bad feat: add object-oriented task manager
f4f3cbb test: add automated output comparison
122fd21 feat: complete Java Inheritance I challenge
7f611da feat: complete Java Method Overriding 2 challenge
```

## Task Object Model

Files:

```text
task-manager/src/com/interview/Task.java
task-manager/src/com/interview/TaskDemo.java
```

`Task` represents one task and stores:

- A title.
- A completion state.
- Behavior for marking the task as completed.

The fields are private so external code cannot directly replace or
modify the object's internal state.

```java
private final String title;
private boolean completed;
```

The constructor establishes a valid initial state:

```java
public Task(String title) {
    if (title == null || title.isBlank()) {
        throw new IllegalArgumentException(
                "Task title cannot be blank.");
    }

    this.title = title.trim();
    this.completed = false;
}
```

The validation establishes the following invariant:

- A successfully created Task has a non-null title.
- Its title is not blank.
- Leading and trailing spaces are removed.
- Its initial completion state is `false`.

### Compile and run

```bash
javac -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/TaskDemo.java

java -cp task-manager/out \
    com.interview.TaskDemo
```

Output:

```text
Title: Review encapsulation
Completed: false
Completed after mark: true
```

## Object-Oriented Task Manager

Files:

```text
task-manager/src/com/interview/TaskManager.java
task-manager/src/com/interview/TaskManagerOopDemo.java
```

Responsibilities:

- `Task` manages one task's data and state.
- `TaskManager` manages a collection of Task objects.
- `TaskManagerOopDemo` demonstrates the program flow and output.

This separation follows the Single Responsibility Principle and
makes each class easier to test, debug, and maintain.

`TaskManager` stores:

```java
private final List<Task> tasks;
```

The field depends on the `List` interface, while the constructor
selects `ArrayList` as the concrete implementation:

```java
this.tasks = new ArrayList<>();
```

This is composition:

```text
TaskManager has a collection of Task objects.
```

### Boundary validation

`completeTask()` validates a human-readable task number before
converting it to a zero-based ArrayList index.

```java
if (taskNumber < 1
        || taskNumber > tasks.size()) {
    return false;
}
```

Test results:

```text
Complete task 2: true
Complete task 99: false
```

### Protecting the internal List

```java
public List<Task> getTasks() {
    return List.copyOf(tasks);
}
```

`List.copyOf()` returns an unmodifiable List snapshot. External code
cannot add, remove, or clear elements through the returned List.

However, this is a shallow copy. Both Lists still contain references
to the same mutable Task objects. Therefore, the snapshot protects
the List structure but does not make each Task immutable.

### Compile and run

```bash
javac -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/TaskManager.java \
    task-manager/src/com/interview/TaskManagerOopDemo.java

java -cp task-manager/out \
    com.interview.TaskManagerOopDemo
```

Output:

```text
Before completion:
1. [ ] Review OOP
2. [ ] Write tests
Complete task 2: true
After completion:
1. [ ] Review OOP
2. [x] Write tests
Complete task 99: false
```

## Automated Output Testing

Files:

```text
task-manager/scripts/run-output-tests.sh
task-manager/test-data/task-demo-expected.txt
task-manager/test-data/task-manager-oop-expected.txt
```

The script:

1. Compiles the required Java files.
2. Runs each demo.
3. Redirects program output to an actual-output file.
4. Compares expected and actual output with `diff -u`.
5. Counts passed and failed tests.
6. Returns a non-zero exit code when any test fails.

Run all tests:

```bash
./task-manager/scripts/run-output-tests.sh

echo $?
```

Successful result:

```text
PASS: TaskDemo
PASS: TaskManagerOopDemo

Result: 2 passed, 0 failed
```

Exit code:

```text
0
```

### Intentional failure verification

The expected title was temporarily changed to an incorrect value.

Result:

```text
FAIL: TaskDemo
PASS: TaskManagerOopDemo

Result: 1 passed, 1 failed
```

Exit code:

```text
1
```

This verified that the script does not display PASS unconditionally
and can detect an actual mismatch.

### `diff` exit codes

- `0`: The files are identical.
- `1`: The file contents are different.
- `2`: An operational error occurred, such as a missing file or
  incorrect path.

### Testing problems encountered

- An expected-output file was initially missing.
- One expected-output file did not end with a newline.
- `diff` returned exit code `2` when a fixture did not exist.
- Zsh `noclobber` prevented `>` from overwriting an existing file.
- The `out/` directory initially contained an unignored actual-output
  file.

The `.gitignore` file was updated with:

```gitignore
out/
```

When using interactive Zsh, existing output files are overwritten
explicitly with:

```bash
>| output-file.txt
```

## HackerRank: Java Inheritance I

File:

```text
hacker-rank/day04/JavaInheritanceI.java
```

Test fixtures:

```text
hacker-rank/day04/test-data/java-inheritance-i-input.txt
hacker-rank/day04/test-data/java-inheritance-i-expected.txt
```

Key concepts:

- `Animal` is the superclass.
- `Bird` is the subclass.
- `Bird extends Animal` is an is-a relationship.
- Bird inherits the accessible `walk()` behavior from Animal.
- A private superclass method cannot be directly accessed by its
  subclass.
- A protected member is accessible within the same package and
  through inheritance in a different package.

Local test:

```bash
javac -d hacker-rank/day04/out \
    hacker-rank/day04/JavaInheritanceI.java

java -cp hacker-rank/day04/out \
    JavaInheritanceI \
    < hacker-rank/day04/test-data/java-inheritance-i-input.txt \
    >| hacker-rank/day04/out/java-inheritance-i-actual.txt

diff -u \
    hacker-rank/day04/test-data/java-inheritance-i-expected.txt \
    hacker-rank/day04/out/java-inheritance-i-actual.txt
```

Result:

- Local compilation passed.
- Expected-output comparison passed.
- HackerRank Run Code passed.
- HackerRank submission passed all tests.

## HackerRank: Java Method Overriding 2

File:

```text
hacker-rank/day04/JavaMethodOverriding2.java
```

Test fixtures:

```text
hacker-rank/day04/test-data/java-method-overriding-2-input.txt
hacker-rank/day04/test-data/java-method-overriding-2-expected.txt
```

Key behavior:

```java
define_me();
this.define_me();
```

Both select the `MotorCycle` override.

```java
super.define_me();
```

This explicitly selects the `BiCycle` implementation for the current
MotorCycle object. `super` is not another object or object reference.

Local test:

```bash
javac -d hacker-rank/day04/out \
    hacker-rank/day04/JavaMethodOverriding2.java

java -cp hacker-rank/day04/out \
    JavaMethodOverriding2 \
    < hacker-rank/day04/test-data/java-method-overriding-2-input.txt \
    >| hacker-rank/day04/out/java-method-overriding-2-actual.txt

diff -u \
    hacker-rank/day04/test-data/java-method-overriding-2-expected.txt \
    hacker-rank/day04/out/java-method-overriding-2-actual.txt
```

Result:

- Local compilation passed.
- Expected-output comparison passed.
- HackerRank Run Code passed.
- HackerRank submission passed all tests.

## Interview Review

### Class, object, and reference

For:

```java
Task task = new Task("Review OOP");
```

- `Task` is the class name and declared type.
- `task` is a reference variable.
- `new Task(...)` is an object creation expression.
- `Task(...)` invokes the constructor.
- `"Review OOP"` is a String literal passed as an argument.

### Constructor, `this`, and `final`

- A constructor initializes a newly created object.
- `this.title` is the current object's instance field.
- `title` is the constructor parameter.
- `final` prevents the field from being reassigned after initialization.
- `final` is not the reason String is immutable.

### Encapsulation

Encapsulation combines data and behavior while restricting direct
access to implementation details.

External code changes Task state through a public API:

```java
task.markCompleted();
```

It does not directly modify the private field.

### Inheritance and composition

Inheritance describes an is-a relationship:

```text
Bird is an Animal.
```

Composition describes a has-a relationship:

```text
TaskManager has a collection of Tasks.
```

### Overriding and overloading

Overriding:

- Occurs through inheritance.
- Uses a compatible method signature.
- Runtime dispatch selects the implementation based on the actual
  object type.

Overloading:

- Uses the same method name with a different parameter list.
- Does not require inheritance.
- Is resolved at compile time.

`@Override` asks the compiler to verify the intended override. It is
not what causes overriding to exist.

### Expected failure and exceptions

An invalid task number is an expected operation failure, so
`completeTask()` returns `false`.

A blank title violates the Task invariant, so the constructor throws
`IllegalArgumentException`.

A caught exception does not necessarily terminate the program.
An uncaught exception normally terminates the current thread.

## Easy-to-Miss Concepts

- A class is not an object.
- A reference variable is not the object itself.
- `List` is an interface; `ArrayList` is an implementation.
- `List.copyOf()` is shallow, not deep.
- An unmodifiable List can still contain mutable objects.
- Inheritance is is-a; composition is has-a.
- `private` members are not directly accessible by subclasses.
- `super` is not another object or reference.
- `@Override` performs compile-time checking.
- Overriding and overloading are different concepts.
- `catch` does not always terminate program execution.
- A test script must derive PASS from a real comparison.
- An intentional mismatch verifies the test harness itself.
- Text fixtures should end with a newline.
- `diff` exit code `2` is not the same as a content mismatch.

## Reflection

### What improved

- `this` and `super` are clearer.
- Overriding and overloading are easier to distinguish.
- Access modifiers are better understood.
- Encapsulation can be explained more precisely.
- Constructors and invariants are more familiar.
- Expected-output testing and shell automation were practiced.

### What still needs practice

- Precise use of class, object, reference, instance, and static.
- Distinguishing fields, parameters, and local variables.
- Describing interfaces, abstractions, and implementations.
- Distinguishing is-a from has-a.
- Explaining shallow copies and mutable elements.
- Describing exception behavior accurately.
- Designing responsibilities across multiple classes.

### Objective evidence

- Blank Task titles were verified to throw
  `IllegalArgumentException`.
- Valid and invalid task completion results were tested.
- The batch script passed two tests with exit code `0`.
- An intentional mismatch produced a failure and exit code `1`.
- Two HackerRank challenges passed all tests.
- Five focused feature commits were pushed successfully.

### Day 5 process adjustments

- Reduce the focused interview review from 10 questions to about 6,
  with up to 8 when targeted follow-up is necessary.
- Keep one or two immediate concept questions after each major
  implementation.
- Use expected-output and scripts according to problem complexity.
- Preserve Git synchronization and quality checks.
- Consolidate repetitive tests into batch execution when useful.
- Always provide the next stage with complete paths, commands,
  expected results, and a checkpoint template.
- Keep enough time available for English practice.