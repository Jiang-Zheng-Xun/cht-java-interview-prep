# Day 27 — Mock Written Test, Answer-strategy Validation, and Presentation Maintenance

## Date

2026-09-13

## Objectives

- Complete a full 30-minute mock written examination.
- Use the expected 40-point multiple-choice and 60-point written-answer structure.
- Evaluate whether daily review performance transfers to a time-limited setting.
- Assess answer order, minimum-complete-answer discipline, completion rate, and technical precision.
- Reinforce no more than two evidence-based primary gaps.
- Maintain Slide 4 retrieval and complete a five-slide rehearsal.
- Preserve presentation v0.3 and the canonical third-version script unless reproducible evidence justifies revision.

## Continuity and Baseline

The current source of truth remained CHT Interview Command Registry v1.5, effective 2026-09-08.

Day 26 had completed its delivery closure with a final effective time of `05:20:25`.

Question Bank verification at the start of Day 27 showed:

- Weak: 66
- Review: 55
- Stable: 6
- Total: 127
- Normalized duplicate groups: 0
- Missing required fields: 0

Git and Maven baseline:

- Base branch: `develop`
- Baseline commit: `5c2d47c`
- Working tree: clean
- Ahead / behind `origin/develop`: `0 / 0`
- Maven tests: 38
- Failures / Errors / Skipped: `0 / 0 / 0`
- Build result: `BUILD SUCCESS`
- Day 26 local and remote feature branches were absent.
- Day 27 local and remote feature branches were absent before branch creation.
- No new formal interview notification had been received.
- No concrete, reproducible, and meaningful implementation or coverage gap was identified.
- The Java Task Manager remained at its verified 38-test baseline.

Day 27 used the feature branch:

`feature/day27-mock-test-answer-strategy-presentation-maintenance`

The branch was pushed earlier than the planned workflow checkpoint. It still pointed to the verified baseline and contained no additional commit, so the deviation caused no artifact or code inconsistency. Future branch instructions should distinguish clearly between local branch creation and the later push stage.

## Full 30-minute Mock Written Examination

### Structure

- Multiple-choice: 10 questions, 40 points
- Written-answer: 5 questions, 60 points
- Total: 100 points
- No intermediate hints or commentary
- Complete answer sheet submitted before grading
- The mock replaced the regular six-question Interview Review

Representative capability coverage included:

- Java and OOP
- Dependency injection and design
- Git and CI
- Secure programming
- SQL
- JDBC and transactions
- HTTP and REST
- Testing

### Timing

- Examination released: `09:20:30 CST`
- Learner started: `09:22:34 CST`
- Learner completed: `09:51:26 CST`
- Pure answer time: `00:28:52`
- Overall examination block: `00:30:56`
- Completion: 15 of 15 questions
- Answer order: sequential from Q1 to Q15
- External references or hints: none

### Score

| Section | Day 21 | Day 27 | Difference |
| --- | ---: | ---: | ---: |
| Multiple-choice | 36 / 40 | 36 / 40 | 0 |
| Written-answer | 40 / 60 | 51 / 60 | +11 |
| Total | 76 / 100 | 87 / 100 | +11 |

The multiple-choice result remained stable. The main measurable improvement was the written-answer section, which increased by 11 points while all questions were completed within the time limit.

## Multiple-choice Review

Nine of ten multiple-choice questions were correct.

Correctly answered capabilities:

- Static methods cannot directly access instance fields.
- Reference type controls compile-time method exposure, while the actual object selects an overridden implementation at runtime.
- Constructor injection receives dependencies from outside the dependent class.
- A commit records a version in the local repository.
- Prepared statements and parameter binding reduce SQL injection risk.
- LEFT JOIN cardinality is based on matching pairs while preserving unmatched left rows.
- A complete business operation should use one transaction and rollback after failure.
- PUT is normally idempotent but not safe.
- CI failures must be investigated rather than ignored.
- Local and CI environment or version differences must be reconciled before merge.

The only incorrect answer concerned testing evidence.

The learner selected a mock verification of `rollback()` invocation as the strongest evidence of final database state. The correct evidence was an independent observer connection querying the database after the failed operation.

A mock or spy directly proves interaction or invocation. An observer connection directly proves the externally visible final state. Calling `rollback()` does not by itself prove that rollback succeeded or that no partial update remained.

## Written-answer Review

### Generics

The learner correctly determined that the original method could not compile because `T` had not been declared. The correct static generic method declaration was:

`public static <T> T chooseFirst(List<T> values)`

A static method can declare and use its own method-level type parameter. The fact that a static method has no `this` is correct but does not directly explain this compilation failure because the original method did not access an instance member.

Score: `10 / 12`

Day 27 provided cross-day, unprompted, time-limited evidence that the core generic-method declaration was understood.

### Dependency Injection and Design

The learner correctly identified that the Controller should depend on a service abstraction and that TaskService should depend on a repository abstraction. Constructor injection allows production implementations and test doubles to be supplied externally.

The initial answer did not clearly identify the composition root. A production object graph should be created by the application entry point, bootstrap code, or another explicit composition root. TaskController should receive and store a service dependency; it should not construct TaskService or SQLiteTaskRepository.

Production wiring can create:

- SQLiteTaskRepository as the TaskRepository implementation
- TaskService as the service implementation
- TaskController with the service abstraction

A Controller unit test can create a fake or mock service and pass it into the Controller constructor without connecting to SQLite.

Score: `8 / 12`

### JDBC Transaction Failure

The learner correctly identified:

- The second INSERT exception as the primary failure
- The rollback exception as a suppressed failure
- The primary exception as the exception that should be rethrown
- The need for an independent observer connection to verify the final committed state

Invocation and exception evidence alone cannot prove the absence of a partial update.

Score: `12 / 12`

### HTTP and Testing Boundary

The learner correctly explained that:

- Controller handles HTTP request validation and response mapping.
- Service owns the atomic business operation and transaction boundary.
- An unexpected server-side transaction failure maps to an HTTP 500 response.
- SQL statements, stack traces, and internal exception messages must not be exposed to the client.
- A failed PUT should be followed by an external observation of the database state.

The strongest formulation explicitly uses an independent observer connection or a subsequent request that sees the externally committed state.

Score: `11 / 12`

### ReDoS

The learner correctly identified nested quantifiers and excessive backtracking as a possible denial-of-service risk. Proposed mitigations included:

- Limiting input length
- Simplifying or rewriting the regular expression
- Applying a timeout
- Using a non-backtracking engine
- Applying rate limits

The remaining improvement was to state measurable validation evidence more precisely:

- Worst-case latency
- Timeout behavior
- CPU and worker-thread consumption
- Processing-time growth as input size increases
- Correct behavior for normal inputs
- Behavior under repeated or concurrent malicious inputs

Score: `10 / 12`

## Four-dimension Evidence

### Concept

Most core concepts were correct. The main timed error was the distinction between invocation evidence and externally observable outcome evidence.

### Wording

Transaction, HTTP, and ReDoS answers contained usable interview-level explanations. Dependency-injection wording needed a clearer separation among abstraction, implementation, dependent object, and composition root.

### Scenario Application

JOIN, transaction failure, HTTP error handling, and ReDoS scenarios were handled well. Testing evidence was correct in the written integration-test answer but incorrect in the multiple-choice outcome-versus-invocation question, showing that timed retrieval was not fully stable.

### English Reading

The learner correctly interpreted the technical terminology used throughout the examination, including static, generic, idempotent, observer, suppressed exception, constructor injection, and backtracking. No primary English-reading obstacle was identified.

## Answer-strategy Assessment

The sequential answer order was effective in this mock:

- All 15 questions were answered.
- Pure answer time was `00:28:52`.
- No written-answer question was left blank.
- The learner preserved key causal statements before adding details.

The minimum-complete-answer strategy produced measurable value. The written-answer score improved by 11 points compared with Day 21.

The refined formal-test strategy is:

1. Quickly scan the examination and identify question distribution and point allocation.
2. Secure stable multiple-choice points efficiently.
3. Begin each written answer with its direct conclusion.
4. Add the minimum causal explanation needed for substantial credit.
5. Expand details only after every written question has a complete core answer.
6. Use remaining time to inspect evidence-boundary questions and technical wording.

## Targeted Reinforcement

Only two evidence-based primary gaps were reinforced.

### Testing Evidence Boundary

Evidence was classified as follows:

- Mock verification of `rollback()` proves invocation.
- Spy evidence that `commit()` was not called proves an interaction did not occur.
- Primary and suppressed exceptions prove the internal failure structure.
- An independent observer connection proves the externally visible final state.

Rollback invocation and observer outcome evidence together support that the rollback path was entered and no external partial update was visible. They do not necessarily prove that successful rollback was the sole cause, especially when rollback itself produced a suppressed failure.

Testing evidence remained Review and requires future cross-day validation.

### Dependency Injection Boundary

The corrected dependency directions were:

- TaskController depends on a service abstraction.
- TaskService depends on a repository abstraction.
- The composition root creates concrete objects and connects dependencies.
- The Controller constructor receives and stores its dependency.
- A unit test creates a fake or mock dependency and passes it into the constructor.

The learner understood the corrected distinction. Mastery was not upgraded solely from same-day reinforcement.

## Mastery Decisions

| Topic | Day 27 decision | Evidence |
| --- | --- | --- |
| Generics | Weak to Review | Cross-day, unprompted, time-limited core answer was correct |
| JOIN cardinality | Review retained | Correct time-limited cardinality result |
| Runtime polymorphism | Review retained | Correct compile-time exposure and runtime implementation selection |
| Git / CI | Review retained | Correct Git-state and CI-failure decisions |
| HTTP / REST | Review retained | Correct semantics, responsibility, status, and safe-response reasoning |
| Transaction failure handling | Strong Review; Stable candidate | Primary, suppressed, rethrow, and observer boundaries were correct |
| Testing evidence boundary | Review; cross-day validation required | Timed multiple-choice error followed by correct same-day reinforcement |
| DI abstraction boundary | No upgrade; cross-day validation required | Composition-root ownership was initially unclear |
| ReDoS | Review evidence | Correct cause and mitigations; measurable test evidence can improve |
| Environment | Weak; appendix / low priority | Not used as a primary mock capability |

Same-day reinforcement did not directly increase Mastery.

## Presentation Maintenance

The presentation continued to use:

- `cht-interview-day23-presentation-v0.3.pptx`
- The canonical third-version five-minute script
- Logos Lab as the correct laboratory name

No presentation or canonical script modification was made.

### Rehearsal Evidence

First slide-anchor practice:

- Start: `15:22:05 CST`
- End: `15:27:38 CST`
- Total: `05:33`
- The learner had recently awakened and needed more time to organize the presentation.

Formal slide-anchor sample:

- Start: `15:32:47 CST`
- End: `15:37:14 CST`
- Total: `04:27`
- Slide 1: `00:32`
- Slide 2: `00:52`
- Slide 3: `00:34`
- Slide 4: `01:31`
- Slide 5: `00:58`
- Slides only; no notes or full script
- No external interruption

Slide-level observations:

- Slide 1 positioning transition was stable.
- Slide 4 included the integration-test and GitHub-delivery anchors.
- The integration-test wording remained less fluent.
- Feature branch and Pull Request were briefly reversed, but the learner immediately corrected the order.
- Slide 5 AI verification and future positioning remained stable.

The correct delivery order remained:

GitHub Issue → feature branch → Pull Request → CI → testing and acceptance → merge to `develop`

The evidence showed a retrieval and fluency issue rather than a content defect. No repeated, concrete, reproducible, and meaningful narrative defect was identified.

Version decision:

- Retain presentation v0.3.
- Retain the canonical third-version script.
- Do not create v0.4.
- Do not create a fourth-version script.

## Learner Reflection

The learner identified the main outcomes as:

- Completing all 15 questions within 30 minutes
- Improving from 76 to 87
- Producing stronger written answers
- Maintaining transaction, HTTP, JOIN, Git / CI, OOP, and security knowledge
- Improving the formal slide-anchor sample from 04:45 to 04:27

The learner attributed the improvement to repeated practice from Day 21 through Day 26, additional personal review and handwritten organization, faster answering, and more time for checking.

The most important technical correction was that the production object graph belongs to the composition root. The Controller constructor receives and stores its dependency rather than constructing concrete implementations.

The learner also confirmed that rollback invocation only proves entry into the rollback path. An observer connection is required to prove that the final externally visible state contains no partial update.

The learner considered the minimum-complete-answer strategy effective and planned to:

- Scan the formal examination first
- Understand question distribution and point allocation
- Secure stable multiple-choice points
- Write the core written-answer factors first
- Add details and perform checks with remaining time

Slide 4 was classified as a retrieval and fluency issue because all required content was present but the technically dense section was slower to organize orally.

The learner agreed to retain presentation v0.3 and the canonical third-version script.

## Workload and Time Record

Effective time before artifact creation:

| Work item | Effective time |
| --- | ---: |
| D27-01 continuity, baseline, and Issue stage gate | 00:13:33 |
| Branch setup | 00:02:24 |
| Branch verification and mock preparation | 00:09:00 |
| D27-02 complete mock examination | 00:30:56 |
| D27-02 stage gate | 00:03:31 |
| D27-03 unified grading and diagnosis | 00:25:58 |
| D27-04 targeted reinforcement | 01:08:09 |
| D27-04 stage gate and lunch decision | 00:42:56 |
| D27-05 presentation rehearsal and version decision | 00:50:00 |
| D27-06 learner-led Reflection | 00:50:09 |
| D27-06 stage gate and dinner decision | 00:18:17 |
| **Effective subtotal before artifact creation** | **05:14:53** |

Excluded time:

- Lunch: `12:12:00–15:18:59` — `03:06:59`
- Haircut / external activity: `16:22:32–17:33:57` — `01:11:25`
- Dinner: `18:28:50–19:51:42` — `01:22:52`
- Total excluded before artifact creation: `05:41:16`

The original target was approximately 3.5 to 4 hours of effective training. The effective subtotal exceeded four hours because unified mock analysis, two targeted reinforcement exercises, stage-gate review, and presentation evidence analysis were more detailed than estimated.

The learner considered the workload manageable. No additional training block was added after the workload limit was reached.

## Day 28 Handoff

Day 28 should prioritize cross-day, unprompted, changed-scenario validation of:

1. Testing evidence boundary
   - Invocation versus externally observable outcome
   - What mock, spy, exception, and observer evidence can each prove
   - Avoiding unsupported causal claims

2. Dependency-injection abstraction boundary
   - Controller-to-service abstraction
   - Service-to-repository abstraction
   - Composition-root ownership
   - Production wiring versus test wiring

The first answer in each topic should determine Mastery. Same-day correction should not be used as upgrade evidence.

Generics should retain Review based on the Day 27 time-limited cross-day result. Transaction failure handling is a strong Review and future Stable candidate. Environment remains appendix / low priority.

Presentation practice should retain v0.3 and the canonical third-version script. Future rehearsal should continue improving Slide 4 retrieval fluency without revising content unless a repeated and reproducible defect appears.

Formal interview notification remains the source of truth for the date, examination format, presentation requirements, and submission rules.

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 27 Notion record.
