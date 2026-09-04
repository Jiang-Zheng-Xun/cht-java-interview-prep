# Day 18 — HTTP／REST 複習、Validation Layers 與面試精煉

## Date

2026-09-04

## Status

Day 18 is fully complete.

- D18-01 through D18-09 are complete
- GitHub Issue #36 is closed
- Pull Request #37 is merged into `develop`
- pull-request CI and post-merge `develop` CI succeeded
- local and remote feature branches were deleted
- no production-code change was required
- the final working tree is clean

## Objective

Consolidate the HTTP／REST, JSON mapping, validation-layer, transaction, and testing concepts implemented on Day 17.

Day 18 prioritizes accurate explanations and concise interview answers instead of adding another large feature.

## Git Baseline

- Repository: `Jiang-Zheng-Xun/cht-java-interview-prep`
- Initial branch: `develop`
- Initial local HEAD: `3bb8d99181ef48edde81bc7e015cef9df69ca57e`
- Initial `origin/develop`: `3bb8d99181ef48edde81bc7e015cef9df69ca57e`
- Initial ahead／behind: `0 0`
- Initial working tree: clean
- Remote branches before Day 18: `main`, `develop`
- Maven configuration:
  - Java 21
  - JUnit 5.10.2
  - SQLite JDBC 3.53.4.0
  - Jackson Databind 2.17.2
- Maven source directory: `task-manager/src`
- Maven test directory: `task-manager/test/src`
- Baseline Maven result: 37 tests, 0 failures, 0 errors, 0 skipped
- Baseline build: `BUILD SUCCESS`
- GitHub Issue: [#36](https://github.com/Jiang-Zheng-Xun/cht-java-interview-prep/issues/36)
- Feature branch: `feature/day18-http-rest-validation-review`
- Feature branch starting commit: `3bb8d99181ef48edde81bc7e015cef9df69ca57e`

## Day 18 Learning Checklist

- [x] D18-01 Verify the Git baseline, Maven configuration, CI, and repository layout
- [x] D18-02 Create and confirm the Day 18 GitHub Issue
- [x] D18-03 Create and publish the Day 18 feature branch
- [x] D18-04 Consolidate HTTP safe／idempotent semantics and representation headers
- [x] D18-05 Trace validation layers and exception-to-response mapping
- [x] D18-06 Refine 30–60 second interview answers using the existing project
- [x] D18-07 Complete the focused Interview Review
- [x] D18-08 Complete learner-led Reflection and create the complete Day 18 Daily Log
- [x] D18-09 Complete commit, PR CI, merge, post-merge `develop` CI, branch cleanup, and Issue closure

## HTTP Safe and Idempotent Semantics

A safe method does not express a client intention to change the resource's business state.

An idempotent method is one where applying the same request once or multiple times is expected to produce the same final resource state.

| Method | Safe | Idempotent | Reason |
|---|---:|---:|---|
| GET | Yes | Yes | Retrieves a resource without requesting a business-state change |
| POST | No | Usually no | Repeated creation may produce multiple subordinate resources |
| PUT | No | Yes | Changes a resource, but the same complete representation should lead to the same final state |
| PATCH | No | Not guaranteed | Idempotency depends on the patch semantics |
| DELETE | No | Yes | Repeated requests still leave the resource absent |

`PUT /tasks` completely replaces the task collection, so it changes business state and is not safe.

Submitting the same representation repeatedly should leave the task collection in the same final state, so the operation is idempotent.

The server may execute `deleteAll()`, inserts, and `commit()` each time without violating HTTP idempotency. Logging, metrics, and other incidental effects also do not determine the final resource state.

## Content-Type and Accept

`Content-Type` describes the representation of the body carried by the current HTTP message. It can describe either a request body or a response body.

`Accept` is sent by the client in a request to describe acceptable or preferred response representations.

A normal GET request usually has no request body:

```http
GET /tasks HTTP/1.1
Accept: application/json
```

Because there is no request body, request `Content-Type` is normally unnecessary. `Accept` may be used to request a JSON response, but it is not mandatory in every GET request.

A PUT request carrying JSON uses:

```http
PUT /tasks HTTP/1.1
Content-Type: application/json
```

In the Day 17 API, `TaskController` validates the PUT request `Content-Type` and returns `415 Unsupported Media Type` when it is not JSON.

The project does not currently implement complete `Accept` content negotiation.

## 204 No Content

`204 No Content` is an HTTP response status indicating that the server successfully processed the request and that the response contains no message body.

The restriction is not limited to JSON; a 204 response must not contain any response representation body.

The project implements this behavior with:

```java
exchange.sendResponseHeaders(204, -1);
```

The causal order is:

```text
HTTP 204 semantics require no response body
→ Java HttpServer must express that behavior
→ the implementation uses a response length of -1
```

The Java argument does not define the HTTP semantics; it implements semantics defined by HTTP.

## Validation Layers

The `PUT /tasks` processing order is:

```text
HTTP path／method／Content-Type
→ request body
→ JSON parsing and DTO mapping
→ domain validation
→ Service business operation
→ Repository SQL operation
→ database constraint
→ commit or rollback
→ Controller HTTP response
```

A request stops at the layer where validation or processing fails.

### Unsupported Content-Type

```text
TaskController media-type validation
→ validation fails
→ 415 Unsupported Media Type
```

This occurs before JSON parsing and before the Service transaction, so rollback is unnecessary.

### Malformed JSON

```text
TaskJsonCodec.deserializeTasks()
→ ObjectMapper.readValue()
→ JsonProcessingException
→ Controller mapping
→ 400 Bad Request
```

This also occurs before the Service transaction.

### Blank Task Title

```text
JSON syntax is valid
→ Jackson creates TaskRequest
→ TaskRequest.toTask()
→ new Task(title)
→ domain validation fails
→ IllegalArgumentException
→ Controller mapping
→ 400 Bad Request
```

The failure is a domain validation failure, not a JSON syntax error.

### Database Failure

```text
Controller opens a Connection
→ TaskService.replaceAllTasks()
→ transaction begins
→ Repository delete or insert fails
→ SQLException
→ Service rollback
→ Service rethrows the exception
→ Controller mapping
→ 500 Internal Server Error
```

The Service performs rollback because it owns the transaction boundary. The Controller maps the failure because it owns the HTTP boundary.

## Controller, Service, and Repository Responsibilities

### Controller

- validates HTTP path, method, and request media type
- reads the request body
- invokes JSON mapping
- calls the Service
- maps results and failures to HTTP responses
- opens and closes request-scoped Connections
- does not directly perform Repository SQL operations
- does not control commit or rollback

### Service

- coordinates the complete business operation
- owns the `replaceAllTasks()` transaction boundary
- disables auto-commit for the transaction
- commits only after all required operations succeed
- rolls back when a transaction operation fails
- restores the original auto-commit state
- does not close the caller-owned Connection

### Repository

- executes individual delete, insert, and query operations
- maps database rows to domain objects
- closes its PreparedStatements and ResultSets
- does not independently commit or roll back the complete business operation

## Public Errors and Server-side Diagnostics

A public HTTP response should contain only a safe status and necessary non-sensitive information.

It should not expose:

- SQL statements
- exception types
- internal exception messages
- stack traces
- implementation details

The Day 17 project returns fixed responses such as:

```json
{"error":"Internal server error"}
```

The current project has completed safe public error mapping but has not implemented:

- server-side logging
- stack-trace preservation
- request or correlation identifiers
- controlled diagnostic records

A production system should retain complete exceptions in controlled server logs while keeping public responses safe.

## Transaction Boundary and Observer Connection

`TaskService.replaceAllTasks()` owns the transaction boundary because the Service knows that `deleteAll()` and all replacement inserts jointly form one indivisible business operation.

All operations must either commit together or roll back together.

An observer Connection provides independent transaction-visibility evidence.

A query using the same transaction Connection may observe its own uncommitted changes. A later GET using a new request-scoped Connection provides stronger evidence that:

- successful replacement data was committed and is visible to other Connections
- a failed replacement did not expose partial changes
- the HTTP read path observes the persisted state

The observer Connection does not exist because the original Connection is incapable of calling or tracking commit. Its value is independent visibility of the resulting database state.

## Interview Answer Structure

Day 18 practiced:

```text
Definition
→ Mechanism
→ Consequence
→ Project example
```

The answer should begin at the abstraction level requested by the question.

For example, an HTTP-semantics question should begin with the protocol definition. Java methods and SQL operations should appear later as project evidence.

The 30–60 second target applies primarily to the first-layer core answer. A real interview topic may continue for several minutes through follow-up questions. For broad questions, the learner should provide the concise core first and allow the interviewer to choose which area to explore.

## Focused Interview Review

### Overall Timing

- Start: 15:33:04 CST
- Completion: 16:25:06 CST
- Overall effective time: 52 minutes 02 seconds
- Pure answer time: 35 minutes 46 seconds
- Feedback and transitions: 16 minutes 16 seconds
- Learner-answer questions: 5

| Question | Answer Time | Result |
|---|---:|---|
| Q1 Safe versus idempotent | 06:17 | Pass |
| Q2 `Content-Type` versus `Accept` | 08:27 | Review |
| Q3 204 semantics versus Java implementation | 04:59 | Review |
| Q4 Public error versus diagnostics | 03:45 | Pass |
| Q5 Transaction boundary and observer Connection | 12:18 | Review |

### Q1 Corrected Answer

Safe determines whether the client's request intent asks to change the resource's business state. Idempotent determines whether applying the same request once or multiple times is expected to produce the same final resource state.

`PUT /tasks` completely replaces the task collection, so it changes business state and is not safe. Repeatedly submitting the same representation should leave the collection in the same final state, so it is idempotent.

### Q2 Corrected Answer

`Content-Type` describes the representation of the body carried by the current HTTP message. `Accept` is sent by the client to indicate acceptable or preferred response representations.

A normal `GET /tasks` has no request body, so it usually does not need request `Content-Type`, although the client may use `Accept: application/json`. A JSON PUT request uses `Content-Type: application/json`.

The project validates PUT request `Content-Type`, but it does not currently implement complete `Accept` negotiation.

### Q3 Corrected Answer

`204 No Content` means that the server successfully processed the request and that the response has no message body of any type.

`sendResponseHeaders(204, -1)` is the Java `HttpServer` implementation of this HTTP behavior. The protocol semantics come first; the Java API call implements them.

### Q4 Corrected Answer

The Controller should return a fixed, safe HTTP status and necessary public message without exposing SQL, exception messages, types, or stack traces.

Complete diagnostics should be stored in controlled server logs. The Day 17 project has implemented safe public error mapping but has not implemented logging, correlation identifiers, or diagnostics preservation.

### Q5 Corrected Answer

`TaskService.replaceAllTasks()` owns the transaction boundary because the Service knows that delete and replacement inserts jointly form an indivisible business operation.

It disables auto-commit, commits after every required operation succeeds, and rolls back when an operation fails.

A later GET using a new Connection provides independent visibility evidence. It demonstrates that successful changes are committed and visible outside the original transaction, or that a failed replacement exposed no partial update.

## Technical Terminology Corrections

| Less precise wording | Correct wording |
|---|---|
| service state | resource state or business state |
| HTTP schematic | HTTP semantics |
| JSONProcessingException | JsonProcessingException |
| 204 has no JSON body | 204 has no response message body |
| Java `-1` causes HTTP 204 semantics | HTTP 204 semantics are implemented with Java `-1` |
| original Connection cannot know commit | observer Connection provides independent committed-state visibility |

`204` is an HTTP status code. `No Content` is its status name or reason phrase. The rule that a successful response contains no message body is its HTTP semantics.

## Learner-led Reflection

### Core Results

- Consolidated HTTP semantics and validation-layer flow.
- Practiced precise descriptions of safe and idempotent methods.
- Reviewed which failures occur at each request-processing layer.
- Reviewed how Controller mappings produce safe HTTP status codes.
- Practiced answering from definitions and responsibility boundaries before implementation details.

### Most Important Technical Understanding

- Safe is explained through request intent.
- Idempotency is explained through the expected final resource state after repeated requests.
- Protocol semantics should be explained before Java or SQL implementation.
- Failures can be classified by their HTTP, JSON, DTO, domain, Service, Repository, or database location.
- Failures occurring before the Service do not require transaction rollback.
- The Service owns transaction recovery, while the Controller owns HTTP mapping.
- The current project implements safe public responses but not server-side logging.

### Improvement from Day 17

The learner now consciously follows this order:

```text
Define the concept
→ explain responsibility or mechanism
→ explain the causal relationship
→ add technical details and project evidence
```

This avoids spending most of the answer explaining low-level implementation before answering the actual question.

### Repeated Errors

- using `service state` instead of `resource state` or `business state`
- using `HTTP schematic` instead of `HTTP semantics`
- reducing general HTTP rules to project-specific behavior
- combining status code, reason phrase, and protocol semantics
- making broad answers too long
- describing evidence without precisely identifying what it proves

### Most Difficult Interview Question

The combined transaction-boundary and observer-Connection question was the most difficult because it required explaining two related but independently substantial concepts:

1. ownership of an atomic business transaction
2. independent Connection visibility of committed state

Future training should normally assess these as separate core questions.

### Answer-Length Strategy

A complete interview topic may reasonably continue for several minutes, especially through follow-up questions.

The initial response should still deliver the central conclusion in approximately 30–60 seconds:

```text
one definition
→ one mechanism or responsibility
→ one consequence
→ one project example
```

Additional details should be provided when the interviewer asks a follow-up or when they are necessary to answer a broad question.

### Workload

Day 18 felt lighter than recent days because it did not include a new implementation feature or extensive implementation comprehension.

This was appropriate after Day 17 exceeded the default workload target.

## Question Bank Review

Recommended mastery updates:

- Safe／idempotent: `Weak` → `Review`
- `Content-Type`／`Accept`: remain `Weak`
- 204 semantics: remain `Weak`
- Validation layers: `Weak` → `Review`
- Public error／diagnostics: `Weak` → `Review`
- Service transaction boundary: `Review` → `Stable`
- Observer Connection: remain `Review`
- Auto-commit: remain `Review`
- HTTP integration versus Service integration: remain `Review`

These changes should be applied only through `/write-notion/question-bank` after the Day 18 learning and Interview Review records are complete.

## Timing

| Item | Time |
|---|---:|
| D18-01 Git baseline | 00:34:59 |
| D18-02 GitHub Issue | 00:07:58 |
| D18-03 Feature branch | 00:11:44 |
| D18-04 HTTP semantics | 01:09:42 |
| D18-05 Validation layers | 00:54:59 |
| D18-06 Answer refinement | 01:05:35 |
| D18-07 Interview Review | 00:52:02 |
| D18-08 Reflection and Daily Log | 01:00:53 |
| D18-09 GitHub delivery | 00:25:32 |
| Final effective training time | **06:23:24** |
| Lunch break | 01:56:14 |
| Dinner break | 00:42:30 |
| Other excluded intervals | 00:53:51 |

Start time: 08:46:22 CST
Final completion time: 18:42:21 CST

Daily Log verification and GitHub delivery are complete. Day 18 was completed at 18:42:21 CST with a final effective training time of 06:23:24.

## Workflow Review

### Effective Corrections

- Git baseline was validated before generating the complete Issue.
- The Issue was confirmed before creating the feature branch.
- Learning Checklist items acted as stage gates.
- GitHub checkbox state was verified instead of relying only on a learner report.
- Working directories and repository-root transitions were stated explicitly.
- Interview Review overall timing and per-question answer timing were separated.
- Reflection occurred before Daily Log creation and delivery.
- No large implementation was added to a reduced-load review day.

### Day-local Correction

The first Daily Log response incorrectly used a triple-backtick outer fence while also containing triple-backtick code examples. This caused the outer Markdown block to terminate early and split the document.

The corrected response used four backticks for the outer fence so that all inner triple-backtick examples remained inside one complete copyable block.

Future Daily Logs containing fenced code examples must use a longer outer fence than every nested fence. This is a formatting enforcement correction under the existing single-complete-block rule and does not require a new Registry proposal.

One Interview Review question combined transaction-boundary ownership and observer-Connection visibility. These concepts are related but independently substantial.

Future reviews should enforce the existing Registry rule that each learner-answer question focuses primarily on one core concept. This is an enforcement correction rather than a new cross-chat Registry proposal.

### No New Registry Proposal

The observed issue is already covered by the live Registry requirement to avoid excessive mandatory subquestions. No new or overlapping Registry rule is required.

## Day 19 Handoff

Day 19 should:

1. continue prompting answers in the order `definition → mechanism → consequence → project example`
2. review validation layers by failure location:
   - HTTP
   - JSON
   - DTO
   - domain
   - Service
   - Repository
   - database
3. integrate related Weak／Review questions only when relevant, without fragmented micro-review
4. reinforce the distinction among HTTP semantics, schema, status code, and reason phrase
5. track Question Bank mastery using repeated evidence instead of one-time correction
6. keep each learner-answer question centered on one primary concept
7. retain the reduced-load lesson when a previous day exceeds the workload target

## Final Delivery

- GitHub Issue: [#36](https://github.com/Jiang-Zheng-Xun/cht-java-interview-prep/issues/36)
- Pull Request: [#37](https://github.com/Jiang-Zheng-Xun/cht-java-interview-prep/pull/37)
- Feature commit: `af98a978d8c525a10ecf0b8702fc10e07eb0d706`
- Merge commit: `8f9ac0be70482553063f1ec5fd4e2989130068be`
- Pull-request CI: completed successfully
- Post-merge `develop` CI: completed successfully
- Local feature branch: deleted
- Remote feature branch: deleted
- Final `develop` ahead／behind: `0 0`
- Final working tree: clean
- D18-01 through D18-09: complete
- Issue #36: closed
- Final completion time: 2026-09-04 18:42:21 CST
- Final effective training time: 06:23:24