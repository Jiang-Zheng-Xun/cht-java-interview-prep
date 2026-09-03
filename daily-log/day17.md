# Day 17 — HTTP、REST API、JSON 與 Controller／Service 分層

## Date

2026-09-03

## Status

Core implementation, Maven validation, HTTP integration tests, Interview Review, and learner-led Reflection are complete.

At the time this log was created:

- GitHub Issue #34 is open
- the Day 17 feature branch has not been committed
- the pull request has not been created
- GitHub pull-request CI has not run
- merge, branch cleanup, and Issue closure remain pending

GitHub delivery must be completed only after this Daily Log is verified.

## Objective

Extend the Java task manager with a lightweight HTTP REST API, JSON request and response mapping, and explicit Controller／Service／Repository responsibility boundaries.

The implementation preserves the existing Service-layer transaction boundary while making the complete HTTP request-to-database flow observable and testable.

## Git Baseline

- Starting branch: `develop`
- Starting commit: `02286a0ad7ebd9d2550833b4e3e1e6ff10ce5288`
- Local `develop` and `origin/develop`: synchronized
- Ahead／behind: `0 0`
- Initial working tree: clean
- Existing Maven suite: 25 tests
- Existing develop GitHub CI: successful
- Day 17 Issue: [#34](https://github.com/Jiang-Zheng-Xun/cht-java-interview-prep/issues/34)
- Feature branch: `feature/day17-http-rest-json-controller-service`
- Feature branch starting commit: `02286a0`

## Day 17 Learning Checklist

- [x] D17-01 Verify the local Git baseline, `pom.xml`, CI workflow, and repository layout
- [x] D17-02 Create the Day 17 GitHub Issue and confirm its number and URL
- [x] D17-03 Create and publish the Day 17 feature branch
- [x] D17-04 Review HTTP request／response structure and REST fundamentals
- [x] D17-05 Implement JSON request／response mapping and HTTP DTOs
- [x] D17-06 Implement Controller／Service separation and the task API
- [x] D17-07 Verify HTTP behavior, database state, and the Maven regression suite
- [x] D17-08 Complete the focused Interview Review
- [x] D17-09 Complete learner-led Reflection and create the complete Day 17 Daily Log
- [ ] D17-10 Complete commit, pull request, CI, merge, branch cleanup, and Issue closure

## HTTP and REST Fundamentals

### HTTP Request

An HTTP request can contain:

- method
- resource path
- protocol version
- request headers
- an optional request body

Example:

```http
GET /tasks HTTP/1.1
Accept: application/json
```

A normal GET request has no request body, so it usually does not need a request `Content-Type`.

`Accept: application/json` means the client wants a JSON response.

For a request that contains JSON:

```http
PUT /tasks HTTP/1.1
Content-Type: application/json
Accept: application/json
```

`Content-Type` describes the representation actually carried by the current request body.

### HTTP Response

An HTTP response can contain:

- protocol version
- status code
- reason phrase
- response headers
- an optional response body

Example:

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
```

A successful `204 No Content` response must not contain a response body.

### Safe and Idempotent Methods

A safe method does not request a change to the resource's business state.

An idempotent method produces the same expected final resource state whether an identical request is applied once or multiple times.

| Method | Safe | Idempotent |
|---|---:|---:|
| GET | Yes | Yes |
| POST | No | Usually no |
| PUT | No | Yes |
| PATCH | No | Not guaranteed |
| DELETE | No | Yes |

`PUT /tasks` is not safe because it changes the task collection.

It is idempotent because repeatedly sending the same complete representation should leave the task collection in the same final resource state, even if the server internally executes delete, insert, and commit operations each time.

## REST API Contract

### `GET /tasks`

Purpose:

- retrieve the current task collection

Successful response:

- status: `200 OK`
- content type: `application/json; charset=utf-8`
- body: JSON task array

### `PUT /tasks`

Purpose:

- completely replace the task collection

Successful response:

- status: `204 No Content`
- body: none

The replacement remains atomic and all-or-nothing through `TaskService.replaceAllTasks()`.

### Error Mapping

| Condition | HTTP status |
|---|---:|
| Malformed JSON | `400 Bad Request` |
| Blank or invalid task data | `400 Bad Request` |
| Unknown path | `404 Not Found` |
| Unsupported method | `405 Method Not Allowed` |
| Unsupported request media type | `415 Unsupported Media Type` |
| Unexpected database or server failure | `500 Internal Server Error` |

Internal SQL statements, exception types, and stack traces are not exposed through public HTTP responses.

## JSON Mapping

Added Jackson Databind:

```xml
<jackson.version>2.17.2</jackson.version>
```

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson.version}</version>
</dependency>
```

### Request Mapping

```text
JSON request body
→ ObjectMapper.readValue()
→ List<TaskRequest>
→ TaskRequest.toTask()
→ List<Task>
```

### Response Mapping

```text
List<Task>
→ TaskResponse.from()
→ List<TaskResponse>
→ ObjectMapper.writeValueAsString()
→ JSON response body
```

### Boundary Objects

- `TaskRequest` represents the JSON accepted from the client.
- `TaskResponse` represents the JSON returned to the client.
- `Task` remains the domain model.
- `TaskJsonCodec` owns JSON serialization and deserialization.
- The domain model does not directly depend on Jackson.

### Validation Layers

Malformed JSON fails during Jackson parsing and raises `JsonProcessingException`.

A blank title can be valid JSON and deserialize into `TaskRequest`, but fails when `TaskRequest.toTask()` calls the `Task` constructor. This is domain validation and raises `IllegalArgumentException`.

A database constraint failure occurs later during an SQL operation and raises `SQLException`.

The layers must not be confused:

```text
JSON syntax
→ DTO mapping
→ domain validation
→ Service business operation
→ Repository SQL operation
→ database constraint
```

## Controller／Service／Repository Responsibilities

### TaskController

Responsibilities:

- inspect the HTTP path
- inspect the HTTP method
- validate `Content-Type`
- read the request body
- invoke JSON mapping
- call `TaskService`
- map results and failures to HTTP responses
- set status codes and response headers
- close caller-owned Connections at the end of the request scope

The Controller does not directly call `SQLiteTaskRepository`.

The Controller also does not call:

- `setAutoCommit(false)`
- `commit()`
- `rollback()`

### TaskService

Responsibilities:

- coordinate task-related business operations
- expose `findAllTasks()`
- preserve `replaceAllTasks()` as the transaction boundary
- ensure replacement is atomic and all-or-nothing
- commit only after every required operation succeeds
- roll back when a required operation fails

The Service knows which Repository operations jointly form one business operation, so it owns the transaction boundary.

### SQLiteTaskRepository

Responsibilities:

- execute individual SQL operations
- map ResultSet rows to domain objects
- close statements and ResultSets created by Repository methods

Repository methods do not independently commit or roll back the business transaction.

### SQLiteDatabase

Responsibilities:

- create SQLite Connections
- enable foreign-key enforcement
- initialize the schema

### Connection Ownership

The component that opens a Connection owns its lifecycle.

For the HTTP request path, the Controller opens and closes the request-scoped Connection. The Service uses that Connection to control the transaction boundary but does not close it.

Closing a caller-owned Connection and deciding commit／rollback are separate responsibilities.

## Implemented Files

Modified:

- `task-manager/pom.xml`
- `task-manager/src/com/interview/TaskService.java`

Added:

- `task-manager/src/com/interview/TaskRequest.java`
- `task-manager/src/com/interview/TaskResponse.java`
- `task-manager/src/com/interview/TaskJsonCodec.java`
- `task-manager/src/com/interview/TaskController.java`
- `task-manager/src/com/interview/TaskHttpServer.java`
- `task-manager/test/src/com/interview/TaskJsonCodecTest.java`
- `task-manager/test/src/com/interview/TaskControllerIntegrationTest.java`

## HTTP Request Flow

### GET

```text
HttpClient
→ HttpServer
→ TaskController
→ database.openConnection()
→ TaskService.findAllTasks()
→ SQLiteTaskRepository.findAll()
→ List<Task>
→ TaskJsonCodec.serializeTasks()
→ 200 application/json
```

### PUT

```text
HttpClient
→ HttpServer
→ TaskController
→ validate Content-Type
→ read request body
→ TaskJsonCodec.deserializeTasks()
→ database.openConnection()
→ TaskService.replaceAllTasks()
→ SQLiteTaskRepository.deleteAll()
→ SQLiteTaskRepository.insertTask() for each task
→ commit or rollback
→ 204 No Content
```

## Automated Tests

### Before Day 17 Changes

```text
Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### JSON Mapping Tests

Added four `TaskJsonCodecTest` cases:

1. serialize tasks as a JSON array
2. deserialize a JSON array as tasks
3. reject malformed JSON
4. reject a blank task title through domain validation

After JSON mapping:

```text
Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### HTTP Integration Tests

Added eight `TaskControllerIntegrationTest` cases:

1. GET returns `200` and a JSON task array
2. PUT returns `204` and persists replacement data
3. malformed JSON returns `400`
4. blank title returns `400`
5. unsupported method returns `405` with `Allow: GET, PUT`
6. unknown path returns `404`
7. unsupported Content-Type returns `415`
8. database failure returns a safe `500` response

HTTP test result:

```text
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Full Maven result:

```text
Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

These are 37 Maven test cases. They are not 37 GitHub CI checks.

A GitHub CI check is one automated workflow or job result that can execute the complete Maven suite.

## Committed-State Verification

The successful PUT test does not stop after receiving `204`.

It sends a later GET through another HTTP request and a new Connection:

```text
JSON
→ TaskRequest
→ Task
→ TaskService transaction
→ SQLite
→ later GET with a new Connection
→ Task
→ TaskResponse
→ JSON
```

Receiving `204` alone only proves that the Controller returned a success status.

The later GET proves that the complete replacement became visible as committed state to another Connection.

The completed state also survives the full JSON → Java → SQLite → Java → JSON round trip.

## Error-Disclosure Verification

The database-failure test deliberately creates an SQLException whose internal message contains SQL:

```text
SELECT secret FROM tasks
```

The public response remains:

```json
{"error":"Internal server error"}
```

The test verifies that the response does not contain:

- SQL text
- `SQLException`
- a stack trace

The purpose is to prevent internal implementation details from leaking to clients. Detailed diagnostics belong in controlled server-side logging, not in the public response body.

## Dependency-Boundary Validation

Searching `TaskController.java` for direct Repository usage produces no result.

Searching for:

- `setAutoCommit`
- `commit(`
- `rollback(`

shows transaction operations only in `TaskService.java`.

`git diff --check` produces no output.

## Interview Review

### Timing

- Overall start: 19:44:10 CST
- Overall end: 20:59:57 CST
- Overall duration: 1 hour 15 minutes 47 seconds
- Learner-answer questions: 6
- Pure answer time: 50 minutes 31 seconds
- Feedback and question intervals: 25 minutes 16 seconds

| Question | Pure answer time | Result |
|---|---:|---|
| HTTP request／response structure | 13:10 | Pass |
| Safe versus idempotent | 08:51 | Review |
| JSON serialization／deserialization and DTOs | 07:21 | Pass |
| Controller／Service／Repository responsibilities | 06:07 | Pass |
| Transaction、auto-commit、partial update | 06:51 | Pass |
| Independent GET and committed state | 08:11 | Pass |

Result:

```text
5 Pass
1 Review
```

### Corrected Interview Concepts

#### HTTP Request and Response

A GET request normally does not need request `Content-Type` because it has no request body. `Accept: application/json` describes the response representation wanted by the client.

The correct resource path is `/tasks`, not `/task`.

#### Safe and Idempotent

Safe and idempotent are HTTP method semantics and must not be defined only through SQLite implementation details.

`PUT /tasks` is idempotent but not safe.

#### Serialization and Deserialization

Serialization:

```text
List<Task>
→ List<TaskResponse>
→ JSON
```

Deserialization:

```text
JSON
→ List<TaskRequest>
→ List<Task>
```

`TaskRequest`, `Task`, and `TaskResponse` must not be treated as the same layer.

#### Transaction Boundary

Service owns the transaction boundary because it knows which SQL operations jointly form the complete business operation.

#### Auto-Commit

With `autoCommit=true`, successful statements are committed individually and a later failure can leave a partial update.

#### Observer／Independent Connection

The transaction Connection can observe its own uncommitted changes while a transaction is active.

A later GET using a new Connection provides stronger evidence that the data is part of committed state visible outside the original transaction.

#### 204 No Content

`204 No Content` has no response body because of HTTP protocol semantics.

`sendResponseHeaders(204, -1)` is the Java implementation mechanism; it is not the reason the protocol forbids a body.

## Learner-led Reflection

Reflection:

- start: 20:59:57 CST
- end: 21:29:36 CST
- duration: 29 minutes 39 seconds

### Core Outcomes

- Completed the full HTTP client → server → Controller → Service → Repository → SQLite integration.
- Established a basic understanding of Controller, Service, and Repository responsibilities.
- Added eight real HTTP integration tests.
- Increased the complete Maven suite to 37 passing tests.

### Most Important Technical Understanding

The most important result was connecting:

- `TaskController`
- `TaskHttpServer`
- `TaskJsonCodec`
- serialization and deserialization
- the Service transaction boundary
- SQLite persistence
- HTTP integration testing

The complete round trip is:

```text
JSON → Java → SQLite → Java → JSON
```

The database-failure test also demonstrated that public HTTP responses must not expose sensitive SQLException details.

### Problems and Corrections

1. Lower-level implementation details were sometimes used to explain higher-level protocol semantics.
   - Improvement: identify whether the question concerns HTTP, application, domain, persistence, or database semantics before answering.

2. Technical terms and exact names were sometimes imprecise.
   - Improvement: deliberately use formal terms such as resource state, representation, protocol semantics, transaction boundary, partial update, and committed state.

3. Answer organization took too long.
   - Improvement: build reusable 30–60 second answer structures and practice recalling them without reconstructing the entire explanation.

4. Validation layers were initially mixed together.
   - Improvement: preserve the explicit sequence of JSON parsing, DTO mapping, domain validation, Repository operation, and database constraint.

### Interview Review Assessment

The main direction of most answers was correct, but confidence and technical precision were insufficient.

The strongest retained concepts were:

- Controller／Service／Repository separation
- Service transaction boundary
- auto-commit and partial update
- independent Connection verification

The largest interview-performance issue was answer duration. Every answer exceeded the intended 30–60 second response window.

### Strongest Performance

The clearest improvement was the foundational understanding of Controller、Service、Repository responsibilities and their interaction with the existing transaction implementation.

### Next Improvements

- improve technical vocabulary and exact class／method／resource names
- distinguish validation layers by when and where failure occurs
- reduce interview answers to a clear definition, mechanism, consequence, and project example
- avoid defining protocol concepts only through implementation details

### Workload Feedback

Recent training days have repeatedly exceeded eight hours of effective work and feel too heavy.

Day 18 should reduce workload, prioritize review and consolidation, and avoid automatically adding another large implementation scope.

## Quantitative Time Summary

### Learning Blocks

| Block | Start | End | Effective time |
|---|---:|---:|---:|
| D17-01 Git Baseline | 08:38:23 | 08:49:24 | 00:11:01 |
| D17-02 GitHub Issue | 08:49:24 | 09:14:47 | 00:25:23 |
| D17-03 Feature Branch | 09:14:47 | 09:26:07 | 00:11:20 |
| D17-04 HTTP／REST Fundamentals | 09:26:07 | 10:30:39 | 01:04:32 |
| D17-05 JSON Mapping | 10:30:39 | 12:00:58 | 01:30:19 |
| D17-06 Controller／Service API | 14:54:06 | 16:33:34 | 01:39:28 |
| D17-07 HTTP Integration Tests | 16:33:34 | 18:28:27 | 01:54:53 |
| D17-08 Interview Review | 19:44:10 | 20:59:57 | 01:15:47 |
| D17-09 Reflection | 20:59:57 | 21:29:36 | 00:29:39 |
| D17-09 Daily Log creation and verification | 21:29:36 | 21:41:54 | 00:12:18 |
| **Subtotal through Daily Log** |  |  | **08:54:40** |

### Excluded Time

| Excluded period | Start | End | Duration |
|---|---:|---:|---:|
| Block interval before lunch | 12:00:58 | 12:20:14 | 00:19:16 |
| Lunch | 12:20:14 | 14:54:06 | 02:33:52 |
| Block interval before dinner | 18:28:27 | 18:43:56 | 00:15:29 |
| Dinner | 18:43:56 | 19:44:10 | 01:00:14 |
| **Total excluded** |  |  | **04:08:51** |

The final effective time will be calculated after Daily Log verification and GitHub delivery are complete.

The default 20:00 target was exceeded with explicit learner approval.

## Strengths

- Completed a working HTTP-to-database architecture.
- Preserved the existing Service transaction boundary.
- Used DTOs to separate the API representation from the domain model.
- Tested normal, invalid, unsupported, and internal-failure paths.
- Verified committed state through a later request and new Connection.
- Correctly distinguished Maven test cases from a GitHub CI check.

## Weaknesses and Review Candidates

Review candidates:

- safe versus idempotent
- request `Content-Type` versus `Accept`
- JSON parsing versus DTO mapping
- domain validation versus database constraint
- 204 protocol semantics versus Java implementation
- safe HTTP 500 responses and internal-detail disclosure
- independent Connection verification of committed state

No item should be promoted directly to Stable based on one day of evidence.

## Workflow Corrections

1. The actual repository path is:

   ```text
   ~/projects/cht-java-interview-prep
   ```

   Do not assume `~/cht-java-interview-prep`.

2. The Issue must contain the numbered D17 learning checklist, including D17-01 baseline.

3. Baseline must finish before the complete Issue Markdown is produced.

4. The feature branch must not be created before the Issue is created and confirmed.

5. The learner records each Interview Review question's start and completion time. Assistant-estimated question start times must not be substituted.

6. Reflection must finish before `daily-log/day17.md` is created.

7. Daily Log must be verified before commit, PR, CI, merge, branch cleanup, or Issue closure.

8. Do not claim that Git workflow or artifacts have no omissions before the delivery checklist has actually been verified.

9. When VS Code detects a Maven build-file change, automatic classpath synchronization may be enabled so new dependencies are recognized.

10. Continue distinguishing Maven test-case counts from GitHub CI-check counts.

## Artifact Checklist Before Delivery

Current status at Daily Log creation:

- [x] HTTP implementation files exist
- [x] JSON DTO and codec files exist
- [x] HTTP integration test file exists
- [x] JSON mapping test file exists
- [x] `pom.xml` includes the Jackson dependency
- [x] 37 Maven tests pass
- [x] `git diff --check` passes
- [x] Interview Review is complete
- [x] learner-led Reflection is complete
- [x] `daily-log/day17.md` has been created
- [ ] Daily Log has been verified in the working tree
- [ ] final pre-commit Maven suite has been rerun
- [ ] commit has been created
- [ ] feature branch has been pushed with implementation changes
- [ ] pull request has been opened
- [ ] pull-request CI has passed
- [ ] pull request has been merged
- [ ] post-merge develop CI has passed
- [ ] feature branch cleanup has completed
- [ ] Issue #34 has been closed

## Day 18 Handoff

Day 18 should be lighter than recent training days and should prioritize consolidation over another large implementation.

Priority review:

1. safe versus idempotent
2. GET request `Content-Type` versus `Accept`
3. JSON parsing、DTO mapping、domain validation、database constraint
4. exact technical naming and concise answer structure
5. relevant Question Bank Weak／Review items selected according to the Day 18 topic

Recommended workflow:

- reduce the learner-answer question count when appropriate rather than automatically using the six-question maximum
- target 30–60 second answers
- use one core concept per question
- avoid adding optional implementation if the core plan approaches the workload limit
- retain the default 20:00 finish guardrail
- explicitly verify every artifact before declaring delivery complete