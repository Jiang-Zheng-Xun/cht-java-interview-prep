# Day 8: Java Collections and Sorting

Date: 2026-08-24

## Daily goals

- Practice choosing between `List`, `Set`, and `Map`.
- Observe duplicate handling and map replacement behavior.
- Review the `equals()` and `hashCode()` contract.
- Practice sorting objects with `Comparator`.
- Complete the HackerRank Java Sort challenge.
- Improve fixture testing by packaging repeated commands in a shell script.
- Continue evaluating the sustainable daily workload.

## Project management

### Issue

- Issue: #15
- Title: `Day 8: Practice Java collections and sorting`

### Branch

```text
feature/day08-collections-sorting
```

The branch started from:

```text
ea9bccb Merge pull request #14 from
Jiang-Zheng-Xun/feature/day07-generics-type-safety
```

### Feature commits

```text
51fa547 feat: add Java collections comparison demo
af48b2e feat: add task sorting demo
005ad2c feat: complete Java Sort challenge
```

## Collections comparison demo

Created:

```text
task-manager/src/com/interview/CollectionsComparisonDemo.java
```

The demo compared:

- `ArrayList`
- `HashSet`
- `HashMap`
- Object identity in `HashSet<Task>`

### List behavior

A `List` preserves each successful insertion and allows duplicate
elements.

```java
taskList.add("Review collections");
taskList.add("Review collections");
```

Result:

```text
List size: 2
List contents: [Review collections, Review collections]
```

### Set behavior

A `Set` prevents logically equal elements from appearing more than
once.

```java
taskSet.add("Review collections");
taskSet.add("Review collections");
```

Result:

```text
Set size: 1
Set contains task: true
```

For `HashSet`, the simplified lookup process is:

1. Use `hashCode()` to identify a candidate bucket.
2. Use `equals()` to check whether an equal element already exists
   in that bucket.

The same hash code does not prove equality. Two objects can have the
same hash code while `equals()` still returns `false`.

### Map replacement behavior

```java
Integer firstPreviousPriority =
        taskPriorities.put("Review collections", 1);

Integer secondPreviousPriority =
        taskPriorities.put("Review collections", 2);
```

Result:

```text
First previous priority: null
Second previous priority: 1
Current priority: 2
Map size: 1
```

`Map.put()` returns the previous value associated with the key:

- New key: usually returns `null`.
- Existing key: returns the replaced old value.
- `get(key)`: returns the current value.

If a Map implementation permits null values, the following result is
ambiguous:

```java
map.get(key) == null
```

It may mean:

- The key does not exist.
- The key exists and maps to null.

Use:

```java
map.containsKey(key)
```

to check whether the key exists.

## Object identity and logical equality

The existing `Task` class does not override `equals()` or
`hashCode()`.

```java
Task firstTask = new Task("Review collections");
Task secondTask = new Task("Review collections");
```

Results:

```text
Same Task reference: false
Tasks equal: false
Task Set size: 2
```

Because `Task` inherits `Object.equals()`, it uses identity equality.
The two constructor calls create different objects, so the two Tasks
are not equal even though their titles are the same.

### Equality concepts

```java
first == second
```

Checks whether both references identify the same object.

```java
first.equals(second)
```

Checks logical equality as defined by the class.

```java
comparator.compare(first, second) == 0
```

Means that the two values are equivalent under that ordering rule.

These three concepts are not interchangeable.

### equals() and hashCode() contract

If:

```java
a.equals(b) == true
```

then:

```java
a.hashCode() == b.hashCode()
```

must also be true.

The reverse is not required. Equal hash codes can be caused by a
collision.

If a class overrides only `equals()` but keeps the default
`Object.hashCode()`, logically equal objects may enter different
hash buckets. A `HashSet` may then fail to remove duplicates, and
hash-based lookup or removal may behave unexpectedly.

### Mutable hash key problem

Fields used by `equals()` and `hashCode()` should normally remain
stable while the object is stored in a hash-based collection.

If a participating field changes:

- The object remains in its original bucket.
- A later lookup may calculate a different bucket.
- `contains()` and `remove()` may fail.
- The collection's lookup structure becomes inconsistent with the
  object's current state.

### Domain identity decision

An immutable title is technically stable enough to participate in
hash calculation, but that does not prove that it is a valid business
identifier.

Two tasks may legitimately share the same title:

```text
Task #101: Review weekly report
Task #205: Review weekly report
```

Equality should be based on the domain rule. If duplicate titles are
allowed, an immutable unique ID is a better identity field.

The Day 8 implementation intentionally did not change `Task.equals()`
or `Task.hashCode()` because the project has not defined title
uniqueness as a business rule.

## Task sorting demo

Created:

```text
task-manager/src/com/interview/TaskSortingDemo.java
```

### Sort by title

```java
Comparator<Task> byTitle =
        Comparator.comparing(Task::getTitle);

tasksByTitle.sort(byTitle);
```

Result:

```text
1. [x] Practice sorting
2. [ ] Review collections
3. [ ] Write tests
```

### Sort by status and title

```java
Comparator<Task> byStatusAndTitle =
        Comparator.comparing(Task::isCompleted)
                .thenComparing(Task::getTitle);

tasksByStatusAndTitle.sort(byStatusAndTitle);
```

`Boolean` natural order is:

```text
false < true
```

Therefore incomplete tasks appear before completed tasks. If two
tasks have the same completion state, `thenComparing()` compares
their titles.

Result:

```text
1. [ ] Review collections
2. [ ] Write tests
3. [x] Practice sorting
```

### Comparator return values

- Negative: the first argument comes before the second.
- Zero: the ordering rule does not distinguish the arguments.
- Positive: the first argument comes after the second.

A Comparator result of zero does not prove object identity or logical
equality.

### Comparable and Comparator

`Comparable<T>` defines a natural order inside the class:

```java
int compareTo(T other)
```

`Comparator<T>` defines an external comparison rule:

```java
int compare(T first, T second)
```

`Task` needs multiple sorting rules, so independent Comparators are
more appropriate than defining one universal natural order.

### Method reference

```java
Task::getTitle
```

is a method reference. It provides existing behavior as a functional
interface implementation and often expresses the same intent more
clearly than an equivalent lambda.

### Shallow copy

```java
List<Task> tasksByTitle =
        new ArrayList<>(originalTasks);
```

This creates a new List structure, but copies the existing Task
references.

Sorting the copied List does not change the element order of
`originalTasks`. However, both Lists still refer to the same mutable
Task objects. Calling `markCompleted()` through either List changes
the shared Task object.

## HackerRank: Java Sort

Challenge:

https://www.hackerrank.com/challenges/java-sort/problem

Created:

```text
hacker-rank/day08/JavaSort.java
```

### Sorting rules

1. CGPA descending.
2. First name ascending when CGPA values tie.
3. ID ascending when both earlier values tie.

Implementation:

```java
Comparator<Student> studentComparator =
        Comparator.comparingDouble(Student::getCgpa)
                .reversed()
                .thenComparing(Student::getFirstName)
                .thenComparingInt(Student::getId);

students.sort(studentComparator);
```

`reversed()` is applied immediately after creating the CGPA
Comparator. Placing it at the end of the full chain would reverse all
three comparison rules.

`comparingDouble()` and `thenComparingInt()` use primitive-specialized
functional interfaces. They avoid unnecessary boxing and clearly
express the compared field types.

### Complexity

Let N be the number of students.

- Input: O(N)
- Sorting: O(N log N) in the general and worst cases
- Output: O(N)
- Total time: O(N log N)
- Stored student data: O(N)
- Sorting auxiliary space: up to O(N)

Java object-list sorting is stable and adaptive. OpenJDK commonly uses
an implementation based on TimSort.

### Local fixtures

Created three focused test cases:

```text
java-sort-sample
java-sort-ties
java-sort-boundary
```

Each case contains:

- One input fixture.
- One expected-output fixture.

Final result:

```text
PASS: java-sort-sample
PASS: java-sort-ties
PASS: java-sort-boundary

Result: 3 passed, 0 failed
```

The ID comparison cannot be observed when two printed first names are
identical, so the source was also checked for:

```java
.thenComparingInt(Student::getId)
```

### HackerRank result

- Run Code: passed
- Submit Code: passed all hidden tests
- Actual editor: minimal `Solution` skeleton
- Submission included `Student` and the complete `Solution`
- Local public class remained `JavaSort`

## Fixture testing workflow improvement

Created:

```text
hacker-rank/day08/scripts/run-java-sort-tests.sh
```

The script:

- Compiles the Java source.
- Runs all three fixtures.
- Overwrites previous actual-output files safely.
- Compares expected and actual output with `diff -u`.
- Displays PASS or FAIL.
- Prints a summary.
- Returns exit code 0 on success and 1 on failure.

Usage:

```bash
hacker-rank/day08/scripts/run-java-sort-tests.sh
echo $?
```

New workflow rule:

```text
One simple test
→ direct execution and diff are acceptable

Two or more fixtures
→ create a reusable test script
```

This avoids repeatedly pasting a long conditional loop into the
terminal and prepares the tests for later CI integration.

## Important commands

### Compile and run Collections Demo

```bash
javac \
    -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/CollectionsComparisonDemo.java

java \
    -cp task-manager/out \
    com.interview.CollectionsComparisonDemo
```

### Compile and run Sorting Demo

```bash
javac \
    -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/TaskSortingDemo.java

java \
    -cp task-manager/out \
    com.interview.TaskSortingDemo
```

### Run Java Sort tests

```bash
hacker-rank/day08/scripts/run-java-sort-tests.sh
echo $?
```

### Git quality checks

```bash
git status --short
git diff --check
git diff --cached --check
git diff --cached --name-only
git rev-list --left-right --count origin/develop...HEAD
git diff --name-status develop...HEAD
```

## Interview review

Six focused topics were reviewed:

1. Choosing `List`, `Set`, and `Map`.
2. The `equals()` and `hashCode()` contract.
3. `HashMap.put()`, `get()`, and `containsKey()`.
4. `Comparable` versus `Comparator`.
5. Comparator chaining, reversing, and stable sorting.
6. Sorting mutation, shallow copying, and complexity.

### Repeated mistakes

- A Set rejects logically equal elements, not automatically all
  objects with the same visible title.
- Hash collection operations are O(1) on average, not unconditionally.
- `hashCode()` returns an `int`, not a boolean.
- `Map.get(key) == null` does not prove that a key is absent.
- `Comparator` is appropriate for multiple external sorting rules.
- A factory method call is not the runtime object type.
- Ordering equality does not imply object identity.
- A stable sort preserves relative order when comparison returns zero.
- `ArrayList` does not use hash buckets.
- The correct names are `equals()`, `containsKey()`,
  `thenComparing()`, and TimSort.

## Time record

```text
Start:                 08:34:31 CST
Lunch break start:     12:00:09 CST
Lunch break end:       14:31:07 CST
Dinner break start:    19:12:06 CST
Dinner break end:      19:56:37 CST
Reflection checkpoint: 20:30:20 CST
```

Calculated checkpoint durations:

```text
Morning training:       3 h 25 m 38 s
Lunch break:            2 h 30 m 58 s
Afternoon training:     4 h 40 m 59 s
Dinner break:           0 h 44 m 31 s
Evening reflection:     0 h 33 m 43 s
Recorded effective work: 8 h 40 m 20 s
```

This value subtracts the two explicitly recorded breaks. Unrecorded
short breaks may make the actual effective time slightly lower.

At the Reflection checkpoint, Day 8 was already 30 minutes 20 seconds
later than the 8:00 PM target. Final integration work remained.

## Reflection

### What improved

- The differences among `List`, `Set`, and `Map` became more concrete.
- Hash-based equality was connected to actual collection behavior.
- Object identity, logical equality, ordering equality, and domain
  identity were separated.
- Multi-level Comparator composition became clearer.
- Fixture testing became easier to repeat after adding a script.
- The completed tests and HackerRank hidden tests provided confidence
  in the implementation.

### What still needs practice

- Determining domain identity from business requirements.
- Avoiding overstatements about boxing and JVM allocation.
- Distinguishing stable ordering from object equality.
- Using precise API and algorithm names.
- Explaining interface methods only after clarifying the question's
  context.
- Remembering that a shallow copy isolates the List structure, not
  mutable element state.

### Interaction feedback

- Progress forms must state whether a value is measured before or
  after an operation.
- Interview questions must clearly identify whether they ask about an
  interface contract, method signature, or usage scenario.
- Six interview topics must not expand into many subquestions and
  repeated follow-ups.
- Multiple fixture tests should be packaged into a script instead of
  pasted as a long terminal loop.
- Creating many fixture files individually in VS Code still has an
  operation cost and should be considered before adding test cases.

### Workload assessment

Day 8 objectively contained more work than Day 7:

- Collections Demo
- Sorting Demo
- HackerRank Java Sort
- Three fixtures and a test script
- Six interview topics with multiple subquestions

The recorded effective-work estimate and completion checkpoint provide
quantitative references, but they do not independently determine
whether the workload is appropriate. The final assessment must be
based on the user's actual physical and mental experience, available
personal time, and explicit feedback.

The user reported that:

- Two Demos increased the implementation time.
- Some concepts were followed by two or three rounds of questions.
- Six interview topics expanded into too many subquestions.
- Day 7 had already reached an acceptable rhythm.
- Additional workload had not been requested.
- Time must remain available for English practice, review, bathing,
  games, and ordinary rest.

Therefore, Day 9 returns to the Day 7 rhythm.

Any future workload change follows this rule:

1. Review quantitative records and the user's subjective experience.
2. Discuss the proposed change with the user.
3. Apply the change only after the user explicitly confirms it.

The assistant may recommend maintaining, increasing, or reducing the
workload, but the user makes the final decision. No experimental
increase should be introduced without prior confirmation.

## Day 9 process decisions

1. Return to one main Demo plus one HackerRank challenge.
2. Do not add a second Demo without explicit agreement.
3. Use concise interview questions with clear context.
4. Avoid multiple rounds of follow-up when the core idea is understood.
5. Label progress-form checkpoints as before or after an operation.
6. Create a shell script when multiple fixtures require conditional test execution.
7. Let the user make the final decision on workload changes.
8. Do not introduce even a trial workload increase without prior discussion and explicit confirmation.