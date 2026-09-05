# Day 19 — CRUD, Transactions, and Integration Validation

## Date

2026-09-05

## Objectives

- Trace the existing task flow from HTTP through Controller, Service, Repository, and SQLite.
- Distinguish public HTTP resource operations from internal SQL CRUD operations.
- Review transaction boundaries, auto-commit, rollback, and partial updates.
- Add one focused HTTP integration test for a transaction failure.
- Verify the externally visible database state through an observer Connection.
- Refine interview explanations of transaction and integration-test evidence.

## Effective Training Time

- Start: 08:17:07 CST
- End of effective training and Reflection: 16:55:17 CST
- Lunch: 11:42:25–14:19:45 CST
- Excluded lunch duration: 2 hours 37 minutes 20 seconds
- Effective training duration: 6 hours 00 minutes 50 seconds

## Baseline

The Day 19 branch started from synchronized `develop` commit `a9cb23a`.

Baseline validation:

- Working tree was clean.
- Local `develop` and `origin/develop` were `0 0` ahead and behind.
- The existing non-standard Maven source and test layout was preserved.
- Maven reported 37 tests with zero failures, errors, or skipped tests.
- The baseline build completed successfully.

## Existing Application Flow

### GET `/tasks`

The read flow is:

`GET /tasks` → `TaskController.handleGet()` → `SQLiteDatabase.openConnection()` → `TaskService.findAllTasks()` → `SQLiteTaskRepository.findAll()` → SQL `SELECT` → `ResultSet` mapping → `List<Task>` → `TaskJsonCodec.serializeTasks()` → `200 OK` with JSON.

Responsibilities:

- Controller handles the HTTP request and response, opens the Connection, serializes the result, and selects the HTTP status.
- Service provides the application boundary and delegates the read operation.
- Repository owns the SQL query and maps database rows to domain objects.
- SQLite persists the data and enforces database constraints.

### PUT `/tasks`

The replace flow is:

`PUT /tasks` → Content-Type validation → request-body reading → JSON parsing and domain mapping → `TaskService.replaceAllTasks()` → disable auto-commit → `deleteAll()` → `insertTask()` for every replacement → commit → restore the original auto-commit state → `204 No Content`.

The HTTP operation is a collection-level Replace or Update. Internally, the Repository uses `DELETE` and multiple `INSERT` statements, but those statements are implementation steps within one business operation. Their presence does not mean the API exposes independent Create and Delete endpoints.

The current HTTP API publicly provides collection Read through `GET /tasks` and collection Replace through `PUT /tasks`. It does not provide complete Create, Read, Update, and Delete endpoints for individual tasks.

## Transaction Boundary and Partial Updates

`replaceAllTasks()` must treat deleting the original tasks and inserting all replacement tasks as one atomic transaction.

Without a transaction, auto-commit could leave states such as:

- All original tasks deleted with no complete replacement.
- The original tasks deleted while only some replacement tasks remain.
- A database state that does not match either the original collection or the requested replacement collection.

The Service therefore saves the original auto-commit state, disables auto-commit, executes the complete operation, commits on success, rolls back on failure, and restores the original setting.

If rollback or auto-commit restoration also fails while another exception is already being handled, the secondary failure is preserved as a suppressed exception while the original failure remains primary.

## Focused HTTP Rollback Integration Test

The new test is:

`putTasksRollsBackWhenSecondInsertFails()`

Modified file:

`task-manager/test/src/com/interview/TaskControllerIntegrationTest.java`

The test first stores `Original task`, then creates a deterministic SQLite trigger:

```sql
CREATE TRIGGER reject_task
BEFORE INSERT ON tasks
WHEN NEW.title = 'Rejected task'
BEGIN
    SELECT RAISE(ABORT, 'rejected task');
END
```

It sends a real `PUT /tasks` request containing:

1. `Replacement task A`
2. `Rejected task`

The expected execution order is:

1. Delete the original task.
2. Insert `Replacement task A`.
3. Reject the second INSERT.
4. Roll back the complete replacement transaction.
5. Map the resulting `SQLException` to a safe HTTP response.

Verified result:

- HTTP status: `500 Internal Server Error`
- Public response body: `{"error":"Internal server error"}`
- The response does not expose the SQL statement, exception type, or internal database message.
- A separate observer Connection reads only `Original task`.
- `Replacement task A` is not externally visible.
- No partial replacement state remains.

## Observer Connection Evidence

The transaction Connection executes the SQL statements, commit or rollback, and auto-commit restoration. Assertions on it can verify its final auto-commit and lifecycle state.

The observer Connection provides independent evidence of the externally visible final database state:

- After a successful commit, replacement data is visible to another Connection.
- After a failed operation and rollback, the original committed data remains visible.
- No partially inserted replacement data is externally visible.

This final-state evidence does not directly prove that a specific JDBC method was called. Direct method-call verification would require a mock or spy Connection.

## Validation Results

Targeted HTTP rollback test:

- Tests run: 1
- Failures: 0
- Errors: 0
- Skipped: 0
- Result: BUILD SUCCESS

Task Controller integration suite:

- Tests run: 9
- Failures: 0
- Errors: 0
- Skipped: 0
- Result: BUILD SUCCESS

Full Maven regression:

- Tests run: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Result: BUILD SUCCESS

The regression suite confirmed that the new failure-path test did not break the existing Repository, Service transaction, JSON, validation, GET, PUT, or HTTP error-mapping behavior.

## Interview Review

Six questions were completed.

- Overall duration: 46 minutes 42 seconds
- Pure answer time: 30 minutes 42 seconds
- Result: four Pass and two Review

Topics reviewed:

- Transaction boundary and partial-update prevention
- JDBC auto-commit
- Observer Connection evidence
- Safe HTTP 500 mapping
- HTTP resource operations versus SQL CRUD
- HTTP integration-test scope versus Service integration-test scope

Strong areas:

- Aligning a transaction boundary with the complete business operation
- Describing a concrete partial-update state
- Distinguishing an HTTP Replace operation from internal DELETE and INSERT statements
- Explaining why internal database diagnostics must not be returned to clients

Review areas:

- An observer Connection proves the externally visible final database state, not direct JDBC method invocation.
- Test-scope comparisons should first identify shared coverage and then explain only the additional boundary.
- Transaction recovery, HTTP mapping, and server-side logging belong to different responsibilities.
- Method names and technical terminology should remain precise.

## Reflection

The main result was connecting the existing HTTP, Controller, Service, Repository, and SQLite layers into one explainable and verifiable flow.

The most important technical understanding was that transaction boundaries must match complete business operations. A replace-all operation is not complete when only DELETE or some INSERT statements succeed.

The focused HTTP integration test extended the evidence from the Service boundary to the public HTTP boundary. It validated real client/server interaction, method, path, Content-Type, request body, JSON mapping, Controller-to-Service interaction, safe status mapping, and the final database state.

The workload was appropriate and the stage-gate process was more stable than the previous two days. No new Registry change was required.

## Workflow Correction

When Maven Surefire selects one test method with `Class#method`, zsh may interpret the unquoted argument before Maven receives it. The argument must be quoted:

`mvn --batch-mode '-Dtest=TaskControllerIntegrationTest#putTasksRollsBackWhenSecondInsertFails' test`

This prevents the shell from producing `no matches found`.

A Daily Log containing an internal fenced code block must be delivered using a longer outer fence so the internal block does not prematurely terminate the copyable document.

## Day 20 Handoff

- Continue refining observer Connection and integration-test scope explanations.
- Keep HTTP error mapping separate from Service transaction recovery.
- Preserve the distinction between public API operations and internal SQL implementation.
- Reuse the verified 38-test baseline.
- Avoid expanding the task-manager project unless a concrete interview or validation gap requires it.

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 19 Notion record.