# Day 14 - SQL Joins, Aggregation, and Subqueries

## Overview

Day 14 extended the Day 13 Task Manager SQL schema with deterministic data for practicing joins, aggregation, grouping, filtering aggregated results, scalar subqueries, and Cartesian-product diagnosis.

The training began on 2026-08-30 and resumed on 2026-08-31 because the original workload was too large for one day.

The main implementation covered:

- `INNER JOIN`
- `LEFT JOIN`
- `COUNT(*)` versus `COUNT(column)`
- `SUM()`, `AVG()`, `MIN()`, and `MAX()`
- `GROUP BY`
- `WHERE` versus `HAVING`
- scalar subqueries
- Cartesian products and unexpected row multiplication
- HackerRank SQL Aggregation
- six SQL interview-review questions

## Repository Baseline

Day 14 started from:

```text
Branch: develop
Commit: 2e1a804
Working tree: clean
```

The feature branch was created as:

```text
feature/day14-sql-joins-aggregation
```

Issue #27 was created with the title:

```text
Day 14: Practice SQL joins, aggregation, and subqueries
```

The feature branch was later connected to its remote upstream with:

```bash
git push -u origin feature/day14-sql-joins-aggregation
```

Verified upstream:

```text
origin/feature/day14-sql-joins-aggregation
```

A checkpoint commit was created and pushed:

```text
127880c feat: add Day 14 SQL joins and aggregation practice
```

## Environment and Workflow Setup

### Day 13 SQL Asset Inspection

The existing SQL files were located with `git ls-files` because `rg` was not initially installed.

Relevant files:

```text
hacker-rank/day13/RevisingTheSelectQueryI.sql
task-manager/sql/day13/task-manager-basics.sql
```

The Day 13 schema contained:

- `users`
- `tasks`
- primary keys
- `tasks.user_id` as a foreign key
- `NOT NULL`
- `UNIQUE`
- `DEFAULT`
- `CHECK`
- deterministic seed data
- `PRAGMA foreign_keys = ON`

Day 13 executed the SQL script against an in-memory SQLite database:

```bash
sqlite3 \
    ':memory:' \
    < task-manager/sql/day13/task-manager-basics.sql
```

### ripgrep

`ripgrep` was not available at the beginning of Day 14:

```text
zsh: command not found: rg
```

It was installed as a lightweight search tool and verified:

```text
ripgrep 13.0.0
-SIMD -AVX (compiled)
+SIMD +AVX (runtime)
```

### VS Code Port Forwarding

VS Code displayed:

```text
Over 20 ports have been automatically forwarded.
The process based automatic port forwarding has been switched
to hybrid in settings.
```

The Ports view displayed 21 auto-forwarded entries, but `ss -ltnp` showed only two actual local listeners:

```text
127.0.0.1:36515
127.0.0.1:33713
```

Both listeners belonged to VS Code Server `MainThread` processes.

The `0.0.0.0:*` value shown under the peer-address column did not mean that a service was listening publicly on every interface.

The final setting retained:

```text
remote.autoForwardPortsSource = hybrid
```

No VS Code Server process was terminated.

### zsh noclobber

The zsh environment had:

```text
noclobber
```

A normal redirection to an existing file therefore produced:

```text
zsh: file exists
```

The reliable overwrite form is:

```bash
>| /tmp/output.txt
2>| /tmp/error.txt
```

This distinction applies to shell redirection and was not a SQLite error.

### Unexpected Space Filename

A zero-byte file whose filename was one space character was found in the repository root.

Inspection confirmed:

```text
Size: 0
File type: empty
Filename: one space character
```

The file was moved out of the repository after confirming its exact identity. The intended Day 14 SQL file was not affected.

## Deterministic JOIN Dataset

The Day 14 SQL exercise was created at:

```text
task-manager/sql/day14/task-manager-joins-aggregation.sql
```

The schema preserved the Day 13 constraints.

Dataset verification returned:

```text
user_count: 4
task_count: 8
foreign_key_violation_count: 0
SQLite exit status: 0
Error bytes: 0
```

User and task distribution:

| User | Task count |
|---|---:|
| Alice | 3 |
| Bob | 3 |
| Carol | 2 |
| Dave | 0 |

Dave was deliberately added without a task so that `INNER JOIN` and `LEFT JOIN` behavior could be compared.

Carol retained a `NULL` `display_name` to preserve the Day 13 NULL example.

## INNER JOIN

The query joined each task to its owner:

```sql
SELECT
    t.id AS task_id,
    u.username,
    u.display_name,
    t.title,
    t.status,
    t.priority,
    t.due_date
FROM users AS u
INNER JOIN tasks AS t
    ON u.id = t.user_id
ORDER BY
    t.id;
```

Result:

```text
Total rows: 8
Alice rows: 3
Bob rows: 3
Carol rows: 2
Dave rows: 0
```

Every `task_id` appeared exactly once.

Dave did not appear because no row in `tasks` had a `user_id` matching Dave's `users.id`.

An important correction was:

> The first expression in `SELECT` does not define output order. Deterministic output requires an explicit `ORDER BY`.

The relationship is:

```text
tasks.user_id -> users.id = many-to-one
users -> tasks              = one-to-many
```

Each task can match at most one user because `users.id` is a primary key.

## LEFT JOIN

The query preserved all users:

```sql
SELECT
    u.id AS user_id,
    u.username,
    t.id AS task_id,
    t.title,
    t.status
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
ORDER BY
    u.id,
    t.id;
```

Result:

```text
Total rows: 9
Alice rows: 3
Bob rows: 3
Carol rows: 2
Dave rows: 1
```

Dave's task columns were:

```text
task_id: NULL
title: NULL
status: NULL
```

`LEFT JOIN` preserves every row from the left table. When no row from the right table matches, the right-side columns are filled with `NULL`.

Reversing the tables would change the preserved side:

```sql
FROM tasks AS t
LEFT JOIN users AS u
```

This would not make Dave appear because the left `tasks` table contains no task belonging to Dave.

## COUNT and NULL Aggregates

The following query compared two forms of `COUNT()`:

```sql
SELECT
    u.id AS user_id,
    u.username,
    COUNT(*) AS joined_row_count,
    COUNT(t.id) AS task_count
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
GROUP BY
    u.id,
    u.username
ORDER BY
    u.id;
```

Result:

| user_id | username | joined_row_count | task_count |
|---:|---|---:|---:|
| 1 | alice | 3 | 3 |
| 2 | bob | 3 | 3 |
| 3 | carol | 2 | 2 |
| 4 | dave | 1 | 0 |

`COUNT(*)` counts joined rows, including Dave's NULL-extended row.

`COUNT(t.id)` counts only rows where `t.id` is non-NULL. It does not perform an equality comparison and does not use SQL `UNKNOWN`; it directly ignores NULL expression results.

## GROUP BY and Aggregate Functions

Priority statistics were calculated for each user:

| username | task_count | total | average | minimum | maximum |
|---|---:|---:|---:|---:|---:|
| alice | 3 | 6 | 2.0 | 1 | 3 |
| bob | 3 | 8 | 2.67 | 1 | 4 |
| carol | 2 | 7 | 3.5 | 2 | 5 |
| dave | 0 | `NULL` | `NULL` | `NULL` | `NULL` |

`GROUP BY` places joined rows with the same grouping values into one group. Aggregate functions then calculate a result for each group.

`SUM()`, `AVG()`, `MIN()`, and `MAX()` ignore NULL inputs. If a group contains no non-NULL input, the result is `NULL`, not zero. `COUNT(t.id)` differs because the count of non-NULL inputs can validly be zero.

## WHERE and HAVING

`WHERE` filters individual rows before grouping and aggregation. `HAVING` filters completed groups based on aggregate results.

When Dave's `t.priority` is `NULL`, `NULL <= 2` evaluates to `UNKNOWN`. A `WHERE` clause retains only `TRUE`; both `FALSE` and `UNKNOWN` are removed.

For users with at least two tasks whose priority was at most 2, row filtering produced:

| User | Matching priorities | Count |
|---|---|---:|
| Alice | 2, 1 | 2 |
| Bob | 1 | 1 |
| Carol | 2 | 1 |
| Dave | none | 0 |

After `HAVING COUNT(t.id) >= 2`, only Alice remained.

When all users must be preserved while matching only `PENDING` tasks, the task filter belongs in the join condition:

```sql
LEFT JOIN tasks AS t
    ON u.id = t.user_id
   AND t.status = 'PENDING'
```

Putting the condition in `WHERE` would remove Dave because `NULL = 'PENDING'` evaluates to `UNKNOWN`.

## Scalar Subquery

The query selected tasks whose priority was above the overall average:

```sql
SELECT
    t.id AS task_id,
    t.title,
    t.priority
FROM tasks AS t
WHERE t.priority > (
    SELECT AVG(priority)
    FROM tasks
)
ORDER BY t.priority DESC, t.id ASC;
```

The average priority was:

```text
21 / 8 = 2.625
```

Result:

| task_id | title | priority |
|---:|---|---:|
| 7 | Read database notes | 5 |
| 4 | Practice Java | 4 |
| 2 | Complete HackerRank | 3 |
| 8 | Check constraints | 3 |

This is a scalar subquery because it returns one value. It is not correlated because the inner query does not reference the outer alias `t` or any value from the outer query's current row. Reading the same base table is not the deciding factor.

## Cartesian Product and JOIN Diagnostics

With 4 users and 8 tasks, a deliberate `CROSS JOIN` returned:

```text
4 * 8 = 32 rows
```

The correct `INNER JOIN` returned 8 rows.

A faulty condition such as `ON u.id = u.id` does not reference the right table and is true for every non-NULL user primary key. It therefore behaves like a Cartesian product.

When a JOIN produces too many rows:

1. Check the JOIN type.
2. Check whether `ON` is missing.
3. Check whether the correct columns are compared.
4. Check all parts of a composite-key relationship.
5. Check JOIN-key uniqueness.
6. Check for duplicate keys or an unexpected many-to-many relationship.
7. Compare row counts before and after joining.
8. Use `GROUP BY ... HAVING COUNT(*) > 1` to find multiple matches.

`LIMIT` does not repair or diagnose an incorrect JOIN. It only hides displayed rows.

## Duplicate Data

Duplicate data must be judged against a defined comparison scope.

- Physical duplicates contain the same values in every compared column.
- Logical duplicates violate an expected uniqueness rule or share the same business key.
- A username repeated across valid one-to-many joined rows is not automatically duplicate data.
- Cartesian-product combinations do not prove that the source tables contain duplicate data.

## HackerRank SQL

Challenge:

```text
Top Earners
```

Accepted solution:

```text
hacker-rank/day14/TopEarners.sql
```

The first attempt grouped by `employee_id`. Since the ID is unique, each group usually contained one row, `COUNT(*)` was 1, and `MAX(months * salary)` returned that employee's own earnings rather than the global maximum.

The accepted solution grouped employees by equal earnings:

```sql
SELECT
    E.months * E.salary AS earnings,
    COUNT(*) AS employee_count
FROM Employee AS E
GROUP BY E.months * E.salary
ORDER BY earnings DESC
LIMIT 1;
```

HackerRank result:

```text
Success
Actual output: 108064 7
```

If a dialect does not allow the SELECT alias in `ORDER BY`, repeat the expression:

```sql
ORDER BY E.months * E.salary DESC
```

No earnings value can be greater than the maximum earnings in the same dataset. A subquery-based comparison must use equality rather than `>`.

## Interview Review

Interview Review time:

```text
Start: 14:28:42 CST
Completion: 15:39:05 CST
Total elapsed time: 1 hour, 10 minutes, 23 seconds
```

Pure answer time:

| Question | Time |
|---|---:|
| JOIN and filter placement | 5:08 |
| COUNT and NULL aggregates | 9:12 |
| WHERE and HAVING | 9:44 |
| Scalar subquery | 8:21 |
| JOIN row multiplication | 12:47 |
| Top Earners debugging | 9:22 |
| Total | 54:34 |

The complete Review time includes answering, reading feedback, correcting concepts, and reviewing explanations.

### Stronger Topics

- `LEFT JOIN` versus `INNER JOIN`
- preserving the left table
- `NULL` and three-valued logic
- `WHERE` versus `HAVING`
- scalar versus correlated subqueries
- recognizing Cartesian products
- explaining the corrected Top Earners grouping strategy

### Topics Requiring More Practice

- distinguishing `COUNT(column)` NULL handling from comparison-based `UNKNOWN`
- calculating group results from the exact input dataset
- explaining uniqueness and JOIN cardinality precisely
- distinguishing repeated joined values from duplicate data
- diagnosing duplicate JOIN keys and many-to-many relationships
- selecting the correct comparison operator around aggregate subqueries
- SQL alias portability

## Learner-led Reflection

### Most Valuable Learning

Day 14 clarified:

- `CROSS JOIN` produces every possible pair
- `LEFT JOIN` preserves the left table
- `INNER JOIN` retains successful matches
- `GROUP BY` creates groups before aggregate calculation
- aggregate functions ignore NULL inputs
- an aggregate with no non-NULL input may return `NULL`
- `WHERE` filters rows before grouping
- `HAVING` filters groups after aggregation
- subquery correlation depends on references to the outer query
- grouping by a unique employee ID differs from grouping by equal earnings

### Most Difficult Areas

The areas requiring the most correction or answer time were:

- JOIN row multiplication
- exact calculation from the provided task data
- duplicate-data terminology
- `COUNT(column)` versus SQL comparisons
- Top Earners grouping logic
- aggregate-subquery comparison operators

These topics should be added to the later interview question bank.

### Workload Evaluation

Day 14 workload was:

```text
too much for one day
```

The Issue combined dataset preparation, JOIN types, aggregation, grouping, row and group filtering, subqueries, Cartesian-product diagnosis, HackerRank, Interview Review, Reflection, documentation, and GitHub delivery. The work therefore extended from 2026-08-30 into 2026-08-31.

### Process Feedback

The following problems were identified:

- completed Issue items did not always receive an immediate checkbox reminder
- some checklist stages lacked clear start/end time reminders
- the feature branch was not pushed upstream immediately after creation
- some expected results were described in prose instead of explicit tables
- a data-dependent interview question omitted its input rows
- one HackerRank link used an incorrect slug
- corrected concept answers were unnecessarily requested again
- the SQL practice was divided into too many small interactions

The preferred review process is:

```text
learner answers
-> direct feedback and correction
-> corrected explanation is recorded automatically
-> understanding is tested later
```

The learner should not be required to rewrite corrected explanations during the same stage.

### Process Decisions

1. Record one start and one end time for each Issue checklist item.
2. Do not time every micro-command.
3. Record lunch, dinner, and other breaks separately.
4. Record each Interview Review question and the complete Review block.
5. Remind the exact Issue checkbox after each completed item.
6. Show explicit input and expected output for implementation exercises.
7. Include necessary tables in data-dependent questions.
8. Push a new feature branch with `git push -u` immediately.
9. Install lightweight trusted tools directly when useful.
10. Reserve controlled setup time for major dependencies.
11. Use one complete block per major topic.
12. Give feedback without requiring an immediate rewritten submission.
13. Verify external exercise links before assigning them.
14. Let Daily Log and Notion capture corrected explanations automatically.
15. Estimate major-task duration before expanding the day's schedule.

## Day 15 Adjustment

Day 15 will retain:

- JDBC fundamentals
- `Connection`
- `PreparedStatement`
- parameter binding
- `ResultSet`
- try-with-resources
- SQLite JDBC through Maven
- SQL injection comparison
- a small deterministic integration test

The schedule should use larger blocks:

```text
environment and dependency verification
-> minimum working JDBC connection
-> parameterized query implementation
-> validation and focused concept review
-> GitHub delivery
```

If Day 14 finishes early, only Day 15 planning and dependency-scope verification should be performed. JDBC implementation should begin in a fresh session.

## Git Record

Day 14 checkpoint commit:

```text
127880c feat: add Day 14 SQL joins and aggregation practice
```

Changes awaiting final delivery:

```text
task-manager/sql/day14/task-manager-joins-aggregation.sql
hacker-rank/day14/TopEarners.sql
daily-log/day14.md
```

The final commit, pull request, merge, branch cleanup, and Issue closure will be recorded after completion.

## Time Record

### 2026-08-30

```text
Day 14 start: 11:05:11 CST
Dataset recovery start: 12:08:01 CST
Dataset rerun: 12:22:34–12:28:46 CST
Lunch completed: 15:19:52 CST
INNER JOIN completed: 16:13:39 CST
LEFT JOIN completed: 16:57:37 CST
COUNT completed: 17:31:18 CST
GROUP BY completed: 18:23:54 CST
Dinner completed: 19:45:51 CST
WHERE and HAVING completed: 20:20:15 CST
Scalar subquery completed: 20:55:26 CST
Checkpoint pushed: 21:13:53 CST
```

Effective training time for 2026-08-30:

```text
5 hours, 55 minutes, 39 seconds
```

### 2026-08-31

```text
Resume session start: 08:45:18 CST
Restroom break: 08:45:18–09:35:47 CST
Ports and tool setup completed: 09:52:06 CST
Cartesian-product practice: 09:52:06–11:12:53 CST
HackerRank Top Earners: 11:22:42–12:21:14 CST
Lunch: 12:22:17–14:27:07 CST
Interview Review: 14:28:42–15:39:05 CST
Reflection: 15:43:46–15:55:26 CST
```

Effective training time for 2026-08-31 through Reflection:

```text
3 hours, 57 minutes, 41 seconds
```

Provisional Day 14 total through Reflection:

```text
2026-08-30: 5 hours, 55 minutes, 39 seconds
2026-08-31: 3 hours, 57 minutes, 41 seconds
Total:       9 hours, 53 minutes, 20 seconds
```

This value is provisional. Daily Log creation, final validation, Git delivery, Issue update, and Issue closure will be added to the final effective training time.