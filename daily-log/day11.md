# Day 11 — Java Regular Expressions and Input Validation

## Goals

- Practice Java regular expressions and input validation.
- Understand `Pattern` and `Matcher`.
- Compare `matches()` with `find()`.
- Practice anchors, groups, quantifiers, and Java regex escaping.
- Validate task commands with explicit input boundaries.
- Complete the HackerRank Java Regex challenge.
- Understand the limits and security risks of regex validation.

## Technical Implementation

### Task Input Validation Demo

Created:

```text
task-manager/src/com/interview/TaskInputValidationDemo.java
```

The demo validates three task commands:

```text
ADD Review regex
COMPLETE 2
LIST
```

Reusable patterns were compiled once:

```java
private static final Pattern ADD_PATTERN =
        Pattern.compile("^ADD \\S(?:.*\\S)?$");

private static final Pattern COMPLETE_PATTERN =
        Pattern.compile("^COMPLETE [1-9]\\d*$");

private static final Pattern LIST_PATTERN =
        Pattern.compile("^LIST$");
```

A separate `Matcher` is created for each input:

```java
private static boolean matches(Pattern pattern, String input) {
    Matcher matcher = pattern.matcher(input);
    return matcher.matches();
}
```

The demo also makes invisible whitespace visible in test output:

```java
private static String displayInput(String input) {
    return input
            .replace("\t", "\\t")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
}
```

Boundary tests covered:

- empty and whitespace-only task titles
- leading and trailing whitespace
- tabs before, inside, and after task titles
- positive, zero, negative, and non-numeric task IDs
- case sensitivity
- unknown commands
- extra content after `LIST`

All local tests passed.

### HackerRank Java Regex

Created:

```text
hacker-rank/day11/JavaRegex.java
```

The HackerRank editor provided a minimal Java 15 `Solution` skeleton rather than the commonly expected locked-code structure.

The official HackerRank problem statement was checked before implementation. It requires a non-public `MyRegex` class containing a `String pattern`.

The single-octet pattern separates the valid range into explicit alternatives:

```java
private static final String OCTET =
        "(?:\\d{1,2}|[01]\\d{2}|2[0-4]\\d|25[0-5])";
```

The complete IPv4 pattern uses one octet followed by exactly three dot-and-octet groups:

```java
String pattern =
        "^" + OCTET + "(?:\\." + OCTET + "){3}$";
```

The implementation supports the HackerRank-specific rule that leading zeros are allowed.

Tests covered:

- `0.0.0.0`
- `255.255.255.255`
- leading-zero forms
- values above `255`
- octets longer than three digits
- missing and extra octets
- incorrect separators
- leading and trailing dots
- non-numeric characters
- extra trailing content
- empty input

The official sample, combined boundary tests, HackerRank Run Code, and all submitted test cases passed.

## Core Concepts

### `Pattern` and `Matcher`

- `Pattern` represents a compiled, immutable regular expression.
- A reusable `Pattern` avoids repeated compilation and centralizes the validation rule.
- `Matcher` binds a pattern to a particular input and stores mutable match state.
- `Pattern` may be safely shared, but the same `Matcher` should not be shared as a mutable constant across unrelated operations or threads.

### `matches()` and `find()`

- `matches()` requires the entire input region to satisfy the regex.
- `find()` searches for the next matching subsequence.
- Complete command validation normally uses `matches()` so that extra content cannot pass through a partial match.

### Anchors

- `^` limits a match to the beginning.
- `$` limits a match to the end.
- Anchors constrain positions; they do not remove or normalize whitespace.
- `trim()` changes a string, while anchors only affect matching.
- Anchors may be redundant with `matches()`, but they can make full-validation intent explicit and keep the regex self-contained.

### Capturing and Non-capturing Groups

- `( ... )` creates a capturing group that can be retrieved with `group(n)`.
- `(?: ... )` groups alternatives or quantifiers without creating a retrievable capture.
- Non-capturing groups avoid unnecessary group numbering.
- Their primary advantage is semantic clarity, not a guarantee of significant memory savings.

### Quantifiers

- `?`: zero or one
- `*`: zero or more
- `+`: one or more
- `{n}`: exactly `n`
- `{n,m}`: between `n` and `m`

In:

```regex
^ADD \S(?:.*\S)?$
```

the first `\S` supports a one-character title. The optional group supports additional title content while requiring the final character to be non-whitespace.

### Java and Regex Escaping

Java strings and regex use separate escaping rules:

```text
Java source
→ Java string parsing
→ regex parsing
```

Examples:

```java
"\\d+"
```

produces the regex:

```regex
\d+
```

and:

```java
"\\."
```

produces the regex:

```regex
\.
```

which matches a literal dot.

### IPv4 Range

IPv4 contains 32 bits divided into four 8-bit octets.

Each octet has the numeric range:

```text
0 to 2^8 - 1
0 to 255
```

The HackerRank problem additionally allows leading zeros and limits each octet to at most three digits.

## Regex Security and Limitations

Regex validates format, but it does not provide complete application security.

It does not replace:

- parameterized SQL queries
- context-aware output encoding
- secure process APIs
- authentication and authorization
- business and semantic validation

ReDoS may occur when ambiguous nested quantifiers or overlapping alternatives cause catastrophic backtracking on specially structured input.

Risk reduction includes:

- avoiding ambiguous nested or overlapping quantifiers
- limiting input length
- using clear and bounded alternatives
- using possessive quantifiers or atomic groups only when they preserve the required matching semantics
- applying external time and resource controls
- considering a regex engine with linear-time guarantees where appropriate

A possessive quantifier can reduce backtracking in a suitable expression, but it cannot guarantee that the entire regex is free from ReDoS.

Java `Pattern` does not provide a simple built-in per-match timeout parameter.

## Interview Review

Six focused topics were reviewed:

1. `Pattern` and `Matcher`
2. `matches()` and `find()`
3. `^` and `$`
4. Capturing and non-capturing groups
5. Java and regex double escaping
6. Regex limitations and ReDoS

### Stronger Topics

- `Pattern` and `Matcher`
- `matches()` and `find()`

### Topics Requiring More Practice

- anchors as positional constraints rather than trimming
- the limits of possessive quantifiers as ReDoS mitigation
- giving every example explicitly requested by an interview question
- avoiding absolute security and performance claims

## Process Feedback

A review question asked for a “precise formula” without specifying whether the expected response was:

- an IPv4 numeric range
- a HackerRank-compatible regex
- a practical IPv4 text representation rule

The wording was ambiguous.

Future questions should:

- state whether the context is HackerRank or general practice
- specify whether the expected answer is a number, formula, regex, Java code, or oral explanation
- avoid indirect confirmation questions when the intended fact can be requested directly

The ambiguous response is not recorded as a conceptual mistake.

The environment did not contain `rg`:

```text
command -v rg
exit code: 1
```

`grep -nE` was used successfully as a compatible source-inspection fallback. No package installation was required.

## Reflection

### Concepts That Became Clearer

- reusable `Pattern` objects
- stateful `Matcher` objects
- full-string versus substring matching
- non-capturing groups
- Java regex double escaping
- IPv4 range decomposition
- combined boundary testing
- regex security limitations

### Concepts Requiring More Practice

- anchors
- possessive quantifiers
- atomic groups
- practical ReDoS mitigation
- concise and precise interview wording

### Workload Evaluation

Day 11 is the second valid workload-observation day.

The final workload decision will use:

- Day 9
- Day 11
- Day 12

No workload adjustment is made before Day 12 is complete and the learner confirms the final decision.

The learner reported a stable condition at the beginning of the day and slight fatigue after dinner.

## Time Record

- Day 11 start: 2026-08-27 08:57:18 CST
- Feature branch setup completed: 09:30:39 CST
- Task Input Validation Demo committed and pushed: 11:42:27 CST
- Lunch and financial matters: 11:43:27～15:19:25 CST
- HackerRank completed: 16:53:40 CST
- HackerRank committed and pushed: 16:57:40 CST
- Interview Review: 16:59:28～17:41:07 CST
- Dinner: 17:45:17～19:13:14 CST

Effective training time before Reflection, Daily Log, and final delivery:

```text
5 hours, 12 minutes, 1 second
```

The final effective training time will be calculated after the pull request, local cleanup, and Issue closure are complete.