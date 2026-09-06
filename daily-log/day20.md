# Day 20 — Integration Boundaries, Error Handling, and Interview Refinement

Date: 2026-09-06

## Goals

- Refine the evidence provided by an observer Connection.
- Compare Service and HTTP integration-test boundaries.
- Separate transaction recovery, HTTP error mapping, and server-side diagnostics.
- Distinguish public API operations from internal SQL implementation.
- Review the existing 38-test coverage before deciding whether to expand the task-manager.
- Convert the reviewed concepts into concise interview answers.

## Baseline

Day 20 started from `develop` commit `df9b233`.

The local and remote `develop` branches were synchronized, the working tree was clean, and the Day 19 feature branch had already been removed.

The Maven baseline was:

- Tests run: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: `BUILD SUCCESS`

The test source layout contains six test source files. Five are JUnit test classes, while `TaskCommandValidatorTest` is an earlier main-based manual test harness.

## Observer Connection Evidence

An observer Connection is independent of the Connection used by the transaction.

In a successful transaction test, the observer Connection can read the complete replacement state. This proves that the final committed result is visible to another Connection.

In a failed transaction test, the observer Connection still reads the original committed state and cannot see the first replacement row as a partial update. This proves that the externally visible final database state is consistent with the expected rollback outcome.

Observer evidence does not directly prove that a specific SQL statement or JDBC method was invoked. It cannot independently verify calls to `DELETE`, `INSERT`, `commit()`, `rollback()`, or `setAutoCommit()`.

Direct method-invocation evidence would require a mock, spy, wrapper, instrumented Connection, or equivalent transaction-side instrumentation.

The transaction Connection provides different evidence. Assertions on `getAutoCommit()` and `isClosed()` verify restoration of the original auto-commit state and preservation of the caller-owned Connection lifecycle.

## Integration-Test Boundaries

The Service integration tests and HTTP integration tests share the following coverage:

- The replace-all business operation
- The Service transaction boundary
- Repository operations
- JDBC
- SQLite
- Deterministic failure produced by a database trigger
- Successful and failed transaction outcomes
- Final persistent database state

The HTTP integration tests add the following boundary:

- Real client/server interaction
- HTTP method and path
- Request Content-Type and message body
- JSON serialization and deserialization
- DTO and domain mapping
- Controller-to-Service interaction
- HTTP status mapping
- The public error-response contract

A test is classified by the real components and boundaries it exercises. Maven and GitHub Actions are test runners and execution environments; they do not determine whether a test is a unit or integration test.

## Error-Handling Responsibilities

### Service

The Service owns the transaction boundary because it understands the complete business operation.

For `replaceAllTasks()`, the Service coordinates deletion of the previous tasks and insertion of all replacement tasks as one atomic operation.

If an operation fails, the Service:

- Rolls back the transaction
- Restores the original auto-commit state
- Preserves the primary exception
- Adds rollback or restoration failures as suppressed exceptions
- Rethrows the primary failure
- Does not close the caller-owned Connection

### Controller

The Controller owns the HTTP boundary.

It is responsible for:

- Validating the HTTP method, path, and Content-Type
- Reading the request body
- Coordinating JSON and domain mapping
- Calling the Service
- Mapping application failures to HTTP status codes
- Returning a fixed and safe public error body

The Controller must not expose SQL statements, internal exception messages, exception types, or stack traces to the client.

### Server-side diagnostics

Server-side diagnostics should retain the complete exception context needed by developers and operators, such as the exception type, message, stack trace, request context, and correlation ID.

The current project implements safe public error responses but does not yet implement server-side logging, correlation IDs, or a centralized error handler.

## Primary and Suppressed Exceptions

The primary exception is the original failure that caused the business operation to fail.

If rollback, cleanup, or auto-commit restoration also fails, that secondary exception must not replace the primary cause. It is attached using `addSuppressed()` so that both the original failure and the recovery failure remain available for diagnostics.

## HTTP Errors Before and Inside the Transaction

A request with an unsupported Content-Type is rejected by the Controller with `415 Unsupported Media Type`. It does not enter the Service transaction.

Malformed JSON or an invalid domain value such as a blank title is rejected with `400 Bad Request` before `replaceAllTasks()` begins.

If the second replacement INSERT throws `SQLException`, the failure occurs inside the Service transaction. The Service performs transaction recovery and rethrows the failure. The Controller then maps it to a safe `500 Internal Server Error`.

If opening the database Connection fails, the Service transaction has not started, so it would be incorrect to claim that the Service performed a rollback.

## Content-Type and Accept

`Content-Type` describes the media type of the current message body.

`Accept` is a request header that describes which response media types the client can accept.

`PUT /tasks` contains a JSON request body and therefore requires `Content-Type: application/json`.

A typical `GET /tasks` request has no request body, so it generally does not need a request Content-Type. A client may use `Accept: application/json` to express the desired response representation.

The current project always returns JSON and does not implement Accept-based content negotiation or `406 Not Acceptable`.

## Public API and Internal SQL

The current public HTTP API provides:

- `GET /tasks` to read the Task collection
- `PUT /tasks` to replace the Task collection

It does not provide complete item-level CRUD endpoints.

At the HTTP layer, `PUT /tasks` represents a collection-level replace operation.

At the Service layer, `replaceAllTasks()` represents one atomic business operation.

At the Repository and SQL layer, that operation is implemented by deleting the existing rows and inserting all replacement rows.

The internal use of `DELETE` and `INSERT` does not imply that clients can call corresponding HTTP Create or Delete endpoints.

A complete item-level CRUD API would normally require operations such as:

- `POST /tasks`
- `GET /tasks/{id}`
- `PUT /tasks/{id}` or `PATCH /tasks/{id}`
- `DELETE /tasks/{id}`

## PUT Idempotency

HTTP idempotency is evaluated from the intended client-visible resource state, not from the number or type of SQL statements executed internally.

Repeated identical `PUT /tasks` requests are expected to produce the same Task collection state even though the implementation executes `DELETE` followed by multiple `INSERT` operations each time.

Regenerated internal row IDs do not necessarily violate idempotency when those IDs are not exposed as part of the public resource representation. If regenerated IDs changed resource URIs or other client-visible behavior, the API semantics would require additional evaluation.

## Coverage Review

The existing tests cover the current requirements and major risks:

- Repository persistence and ordering
- Parameter binding and injection-shaped data
- Database constraints
- Successful replace-all transactions
- Failed replace-all transactions
- Auto-commit restoration
- Preservation of caller-owned Connections
- Successful HTTP PUT and persistence
- HTTP rollback after deterministic INSERT failure
- Safe HTTP 500 responses
- Preservation of the original committed state
- Absence of an externally visible partial update
- HTTP 400, 404, 405, and 415 behavior

Direct commit and rollback invocation tests would be an optional interaction-test enhancement.

Server-side logging and correlation IDs would be optional diagnostics enhancements.

Item-level CRUD endpoints would be a new feature outside the current application scope.

The coverage review found no concrete, reproducible, and meaningful gap in the current requirements. The project therefore retained the 38-test baseline without adding production code or duplicate tests.

## Interview Review

The Interview Review contained six learner-answer questions.

| Topic | Answer time | Result |
|---|---:|---|
| Observer Connection evidence | 09:19 | Pass |
| Service vs HTTP integration-test scope | 13:39 | Pass |
| Error responsibility boundaries | 10:32 | Pass |
| Public API vs SQL implementation | 05:16 | Pass |
| Content-Type vs Accept | 07:58 | Review |
| Evidence-based coverage decision | 08:44 | Review |

The total learner answer time was 55 minutes 28 seconds.

The full Interview Review, including answers, feedback, corrected versions, and transitions, took 1 hour 13 minutes 39 seconds.

## Strengths

- Observer Connection evidence is now clearly separated from direct method-invocation evidence.
- Shared Service and HTTP integration-test coverage is identified before describing the additional HTTP boundary.
- Service transaction recovery, Controller HTTP mapping, and diagnostics are separated by responsibility.
- Public API operations are no longer inferred directly from Repository or SQL operations.
- The decision not to expand the application was based on requirements, risks, and evidence rather than test count.

## Areas for Continued Review

- `Accept` describes response representations acceptable to the client.
- Outcome evidence must not be described as direct invocation evidence.
- Test sufficiency depends on requirements, risks, and acceptance evidence rather than the number of tests.
- Precise technical language should replace contradictory expressions.
- Interview responses should begin with the conclusion and then give two supporting reasons.

## Reflection

The most important result of Day 20 was a clearer understanding of evidence and responsibility boundaries.

Observer Connection tests verify externally visible final database state but do not directly prove internal JDBC method calls.

The Service owns the complete transaction operation and recovery. The Controller owns HTTP request and response semantics. Detailed exception information belongs in protected server-side diagnostics.

The current public API exposes collection read and replace operations. Internal SQL DELETE and INSERT operations do not imply that the API exposes complete CRUD endpoints.

The task-manager was not expanded because the existing requirements and principal risks already have sufficient acceptance evidence.

Some unrecorded interruptions occurred while watching a competition and caring for a child. Future personal interruptions should be recorded immediately as separate breaks so that effective training time remains accurate.

The next phase should prioritize full review and simulation, using the results to identify weak areas. Preparation should include a timed mock written test, a five-minute presentation, a five-minute self-introduction, a thirty-minute interviewer Q&A simulation, targeted reinforcement of weak topics, and stable review of existing strengths.

## Time Summary

| Block | Duration |
|---|---:|
| D20-01 Continuity and baseline | 00:21:14 |
| D20-02 Issue and branch setup | 00:08:38 |
| D20-03 Observer Connection and test scope | 01:23:46 |
| D20-04 Error responsibility boundaries | 01:02:10 |
| D20-05 API operation vs SQL implementation | 01:27:11 |
| D20-06 Coverage review | 01:08:47 |
| D20-07 Interview Review | 01:13:39 |
| Reflection | 00:43:11 |
| Total effective training | 07:28:36 |

Recorded breaks:

- Lunch: 12:00:50–14:37:33 CST, 2 hours 36 minutes 43 seconds
- Dinner: 18:49:21–20:07:35 CST, 1 hour 18 minutes 14 seconds

The planned 20:00 guardrail was exceeded. The remaining closure work continued only after explicit learner approval. No additional technical learning or task-manager expansion was added after the guardrail.

## Day 21 Handoff

- Begin full-topic review and simulation instead of expanding the current application by default.
- Use a timed mock written test to identify remaining Weak topics.
- Continue short-answer practice for `Accept`, evidence boundaries, and coverage decisions.
- Begin planning the five-minute interview presentation and self-introduction.
- Prepare for a thirty-minute interviewer Q&A simulation.
- Add new implementation only when review or simulation reveals a concrete requirement or evidence gap.

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 20 Notion record.
