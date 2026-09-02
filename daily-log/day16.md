# Day 16 — JDBC Transactions, Rollback, and Service Layer

## Date

2026-09-02

## Learning Objectives

- Understand JDBC auto-commit and explicit transactions
- Define a transaction boundary at the service layer
- Keep Connection ownership outside repository methods
- Commit only after the complete business operation succeeds
- Roll back all changes when any operation fails
- Restore the original auto-commit state
- Verify commit and rollback through integration tests
- Deliver the implementation through Pull Request and CI

## Implementation

### SQLiteTaskRepository

- Added `DELETE_ALL_TASKS`
- Added `deleteAll(Connection)`
- Kept Connection ownership outside the repository
- Closed repository-owned `PreparedStatement` with try-with-resources
- Avoided `commit()`, `rollback()`, and `Connection.close()` inside repository methods

### TaskService

- Added `TaskService`
- Added `replaceAllTasks()`
- Validated the Connection and replacement task list before starting the transaction
- Copied the replacement list to prevent structural changes by the caller
- Saved and restored the original auto-commit state
- Used one Connection for `deleteAll()` and all `insertTask()` operations
- Committed only after every operation succeeded
- Rolled back on `SQLException` and `RuntimeException`
- Preserved rollback failures with `addSuppressed()`
- Left Connection lifecycle ownership with the caller

## Integration Tests

- Added `deletesAllTasksAndReturnAffectedRowCount()`
- Added `TaskServiceIntegrationTest`
- Added `commitsAllReplacementTasks()`
- Added `rollsBackAllChangesWhenReplacementFails()`
- Used an observer Connection to verify committed database state
- Used a SQLite trigger to make the second replacement INSERT fail deterministically
- Verified that rollback removed the first replacement INSERT and preserved the original task
- Verified that the transaction Connection remained open
- Verified that the original auto-commit state was restored

## Validation

- Repository targeted tests: 6 passed
- TaskService targeted tests: 2 passed
- Full Maven test suite: 25 passed
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: SUCCESS
- `git diff --check`: passed
- Pull-request CI: passed

## Interview Review

1. A transaction groups multiple database operations into one all-or-nothing unit.
2. With auto-commit enabled, each SQL statement is committed independently and may leave a partial update.
3. The service layer owns the transaction boundary because it coordinates the complete business operation.
4. An observer Connection verifies externally visible committed state rather than uncommitted changes visible only to the transaction Connection.

Interview questions passed: 4 / 4
Actual answering time: 25 minutes 53 seconds

## GitHub Delivery

- Issue: #31
- Implementation commit: `409450f`
- Pull Request: #32
- Merge commit: `d70f1a5`
- CI workflow: Java CI with Maven
- CI checks: 1
- CI result: SUCCESS

## Time Summary

- Effective training time through D16-07: 4 hours 51 minutes 21 seconds
- D16-08 overall time: 41 minutes
- Effective time through D16-08: 5 hours 32 minutes 21 seconds
- Final delivery time is recorded in the Day 16 session summary

## Reflection

Day 16 connected JDBC operations with a service-layer transaction boundary. The main result was understanding that repository methods should execute individual SQL operations, while the service coordinates the complete business operation and decides when to commit or roll back.

The implementation demonstrated all-or-nothing task replacement, restoration of the original auto-commit state, preservation of rollback failures, and integration tests that distinguish committed state from changes visible only inside the transaction Connection.

The transaction concepts are generally understood. The next improvement area is using more precise interview terminology, especially atomicity, transaction boundary, partial update, committed state, and Connection ownership.