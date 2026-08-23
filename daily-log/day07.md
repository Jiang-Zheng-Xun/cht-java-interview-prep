# Day 7 — Java Generics and Type Safety

Date: 2026-08-23
Issue: #13
Branch: `feature/day07-generics-type-safety`

## Goals

- Understand generic classes and generic methods.
- Practice compile-time type safety.
- Understand type inference, checking, and erasure.
- Learn generic invariance and bounded wildcards.
- Complete HackerRank Java Generics.
- Continue evaluating the reduced daily workload.

## Schedule

- Start: 08:48:15 CST
- Lunch and rest: 11:28:09–14:31:30 CST
- Dinner and rest: 18:16:17–18:57:40 CST
- Final completion: recorded after merge and cleanup

## Deliverables

Created:

```text
task-manager/src/com/interview/GenericStore.java
task-manager/src/com/interview/GenericStoreDemo.java
hacker-rank/day07/JavaGenerics.java
```

Feature commits:

```text
eeb8947 feat: add generic store demo
cb6244c feat: complete Java Generics challenge
```

## GenericStore<T>

```java
public class GenericStore<T> {
    private final List<T> items =
            new ArrayList<>();

    public void add(T item) {
        items.add(
                Objects.requireNonNull(
                        item,
                        "item must not be null"));
    }

    public T get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public List<T> getAll() {
        return List.copyOf(items);
    }
}
```

The same generic class was used as:

```java
GenericStore<Task> taskStore =
        new GenericStore<>();

GenericStore<String> textStore =
        new GenericStore<>();
```

The compiler prevents values of the wrong type from being added.

The demo also verified:

- Task and String stores
- Concrete return types without casts
- Null rejection
- Unmodifiable List snapshots

## Terminology map

For:

```java
GenericStore<Task> taskStore =
        new GenericStore<>();
```

- `GenericStore<T>`: generic class declaration
- `T`: type parameter
- `GenericStore<Task>`: parameterized type
- `Task`: type argument
- `taskStore`: reference variable
- `new GenericStore<>()`: constructor call
- `<>`: diamond operator; compiler infers `Task`

For:

```java
public <T> void printArray(T[] array)
```

- `<T>`: method type parameter declaration
- `T[] array`: method parameter
- `integerArray`: method argument at invocation
- Compiler infers `T = Integer` from `Integer[]`

## Class and method type parameters

A class type parameter applies to members using the class-level
`T`:

```java
private T value;
public void add(T item);
public T get();
```

A method type parameter applies only to that method:

```java
public <T> void printArray(T[] array);
```

Each invocation can infer a different type:

```text
Integer[]   → T = Integer
String[]    → T = String
Double[]    → T = Double
Character[] → T = Character
```

Java does not normally generate a separate class for each type
argument.

## List<Object> versus GenericStore<T>

`List<Object>` can mix different reference types:

```java
items.add("Java");
items.add(Integer.valueOf(10));
items.add(new Task("Review"));
```

Its `get()` returns `Object`, so a caller may need a cast.

A cast is an explicit type assertion, not proof of the runtime
type. An incompatible object causes `ClassCastException`.

`GenericStore<String>` only accepts String and returns String,
providing compile-time type safety without caller casts.

## Type inference, checking, and erasure

All three occur during compilation.

### Type inference

The compiler infers type arguments from:

- Method arguments
- Assignment or target context
- Constructor context

### Type checking

The compiler verifies that operations obey the inferred or
declared generic types.

### Type erasure

After checking, the compiler removes most concrete generic type
arguments while generating bytecode.

An unbounded `T` is generally erased to `Object`; a bounded `T`
is erased to its upper bound. The compiler inserts casts where
needed.

Runtime usually sees `GenericStore`, not the complete
`GenericStore<Task>` type argument.

Because `T` is not normally reified at runtime, code generally
cannot directly use:

```java
new T()
value instanceof T
```

A runtime factory can instead be supplied through `Class<T>` or
`Supplier<T>`.

## Primitive and wrapper types

Generic type arguments must be reference types.

Invalid:

```java
GenericStore<int>
List<double>
```

Valid:

```java
GenericStore<Integer>
List<Double>
```

Common wrappers:

| Primitive | Wrapper |
|---|---|
| `int` | `Integer` |
| `long` | `Long` |
| `double` | `Double` |
| `float` | `Float` |
| `boolean` | `Boolean` |
| `char` | `Character` |
| `byte` | `Byte` |
| `short` | `Short` |

Autoboxing converts a primitive to its wrapper where required;
unboxing converts the wrapper back to a primitive.

`int[]` is an object and can be assigned to `Object`, but it is
not `Object[]` because its elements are primitives.

## Generic invariance

Although:

```text
Integer is-a Number
```

this is not valid:

```java
List<Number> numbers =
        new ArrayList<Integer>();
```

If it were allowed, code could add a Double through the
`List<Number>` reference and corrupt the original Integer list.

Therefore Java generic types are normally invariant.

## Bounded wildcards and PECS

### `? extends Number`

```java
List<? extends Number> source;
```

The actual element type may be Integer, Double, or another
Number subtype.

It can safely produce:

```java
Number value = source.get(0);
```

It normally cannot safely accept an Integer because the actual
List may be `List<Double>`.

### `? super Integer`

```java
List<? super Integer> destination;
```

The actual element type may be Integer, Number, or Object.

It can safely consume:

```java
destination.add(Integer.valueOf(10));
```

Reading only guarantees:

```java
Object value = destination.get(0);
```

PECS:

```text
Producer Extends
Consumer Super
```

Wildcard write restrictions come from compile-time wildcard
checking, not from type erasure.

## Objects.requireNonNull()

```java
items.add(
        Objects.requireNonNull(
                item,
                "item must not be null"));
```

Java evaluates the argument before invoking `items.add()`.

If the item is null, `requireNonNull()` throws
`NullPointerException`, normal flow stops, and `items.add()` is
never called.

The text is an exception message, not automatically printed
output.

## HackerRank — Java Generics

Challenge:

```text
https://www.hackerrank.com/challenges/java-generics/problem
```

One generic method prints arrays of different reference types:

```java
public <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.println(element);
    }
}
```

Method overloading was not used.

The actual editor provided only imports and an empty
`Solution.main()`. The submission therefore completed:

- Printer class
- Generic printArray method
- Integer and String arrays
- Printer object
- Both method calls

HackerRank Run Code and hidden tests passed.

## Local testing

Official output:

```text
1
2
3
Hello
World
```

Additional types:

```text
1.5
2.5
A
B
```

The tests covered Integer, String, Double, and Character.

No fixture files or batch script were created because the
program has no input branches and deterministic output. Direct
execution provided sufficient coverage at lower maintenance
cost.

## Focused interview review

Six questions covered:

1. Class type parameters versus method type parameters
2. Type inference versus type erasure
3. Primitive types, wrappers, and autoboxing
4. Generic invariance
5. Upper-bounded wildcard `? extends`
6. Lower-bounded wildcard `? super` and PECS

## Repeated mistakes

- `T` is a type parameter; `Task` in `GenericStore<Task>` is a
  type argument.
- `GenericStore<Task>` is a parameterized type, not a separate
  implementation class.
- Type inference and erasure both occur during compilation.
- `List<Object>` accepts String, Integer, Task, and other
  reference objects.
- Casts do not guarantee runtime compatibility.
- `int[]` is an object but is not `Object[]`.
- Generic invariance prevents unsafe writes.
- Wildcard restrictions come from the unknown captured type.
- `? extends` is mainly a producer.
- `? super` is mainly a consumer.
- Reading from `? super Integer` only guarantees Object.
- Java uses `remove()`, not `delete()`, for List deletion.

## Useful commands

Compile and run Generic Store:

```bash
rm -rf task-manager/out
mkdir -p task-manager/out

javac \
    -d task-manager/out \
    task-manager/src/com/interview/Task.java \
    task-manager/src/com/interview/GenericStore.java \
    task-manager/src/com/interview/GenericStoreDemo.java

java \
    -cp task-manager/out \
    com.interview.GenericStoreDemo
```

Compile and run Java Generics:

```bash
rm -rf hacker-rank/day07/out
mkdir -p hacker-rank/day07/out

javac \
    -d hacker-rank/day07/out \
    hacker-rank/day07/JavaGenerics.java

java \
    -cp hacker-rank/day07/out \
    JavaGenerics

java \
    -cp hacker-rank/day07/out \
    JavaGenerics \
    local
```

Commit checkpoint:

```bash
git status --short --untracked-files=all
git add <exact-paths>
git status --short
git diff --cached --name-only
git diff --cached --check
git commit -m "<message>"
git push
```

## Reflection

The clearest improvements were:

- Class versus method type parameters
- Wrapper classes and autoboxing
- `? extends`, `? super`, and PECS

The main concepts requiring more practice are:

- Type inference versus type erasure
- `int[]` versus `Object[]`
- Wildcard write restrictions
- Precise use of parameter, argument, declared type, runtime
  object type, type parameter, and type argument

The direct testing scope was sufficient and more efficient than
creating unnecessary fixtures.

Future HackerRank exercises will assume a minimal skeleton as
the working default, but still perform a quick visual check.
A separate skeleton checkpoint is only needed when locked code
or special provided classes differ from that default.

The approximately 7–7.5 hour workload was acceptable and still
left room for English study and personal time.

Day 8 will maintain the current workload. Whether to increase
the long-term target to 7–8 hours will be decided after more
days of observation.