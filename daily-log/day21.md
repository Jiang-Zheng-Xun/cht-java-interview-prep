# Day 21｜全範圍模擬、弱點診斷與一面準備

## Date

2026-09-07～2026-09-08

## Goal

Run a full-scope first-round simulation across Java, Collections, exceptions, testing, SQL, JDBC, transactions, HTTP／REST, JSON, application layering, and Git workflow.

Use timed written-test and interviewer-Q&A evidence to diagnose remaining weaknesses, refine reusable interview answers, and prepare a five-minute presentation and self-introduction.

Preserve the existing 38-test baseline unless a concrete, reproducible, and meaningful implementation or coverage gap is verified.

## Completed Work

Day 21 completed the following learning outcomes:

- Confirmed the Day 21 Git and Maven baseline.
- Created the Day 21 Issue and feature branch.
- Completed a timed 30-minute, 100-point mock written test.
- Diagnosed cross-topic Weak／Review areas from actual answers.
- Established a time-bounded strategy for the first-round written test.
- Planned a five-slide, five-minute presentation and self-introduction.
- Completed three timed self-introduction readings.
- Completed a six-question interviewer Q&A simulation.
- Preserved corrected interview answers without requiring immediate rewrites.
- Reinforced the newly identified unfamiliar-system and AI-usage weaknesses.
- Confirmed that no verified implementation or coverage gap was found.
- Preserved the task-manager and its 38-test baseline.

## Mock Written Test

The mock written test used the expected first-round structure:

- Multiple-choice questions: 10 questions, 40 points
- Written-answer questions: 6 questions, 60 points
- Time limit: 30 minutes
- Coaching during the attempt: none
- Unanswered questions: 0

Results:

| Section | Score |
|---|---:|
| Multiple choice | 36／40 |
| Written answers | 40／60 |
| Total | 76／100 |

The attempt started at 2026-09-07 09:44:19 CST and was submitted at 10:14:00 CST, using 00:29:41.

The adopted written-test strategy is:

1. Complete the certain multiple-choice questions first.
2. Check negation, compile-time versus runtime behavior, and answer direction.
3. For each written answer, first provide the minimum complete answer.
4. Add reasons, process details, and project evidence only when time remains.
5. Reserve final time to confirm that no question was omitted.

A minimum complete answer should prioritize the conclusion and essential technical terms rather than attempt to reproduce a long model answer under time pressure.

## Mock-Test Diagnosis

The main Weak candidates identified by the mock written test were:

- Static method versus instance method
- JOIN cardinality and row multiplication

The main Review candidates were:

- Interface-oriented method parameters
- Transaction recovery completeness
- Evidence-based coverage decisions
- Precise technical terminology

Stable evidence was observed for:

- Externally visible outcome evidence versus invocation evidence
- Service, Controller, and server-side diagnostics responsibilities
- Time management under the 30-minute written-test limit

Earlier topics showed memory decay. Future review should therefore use cross-topic retrieval practice rather than rereading topics strictly in Day order.

## Static and Instance Boundary

A static method belongs to the class and has no specific object instance.

Therefore, it cannot directly access an instance field. Attempting to do so produces a compile-time error rather than a runtime exception.

Minimum interview answer:

> A static method has no specific instance, so it cannot directly access an instance field. This is a compile-time error.

## Interface-Oriented Parameter

A parameter declared as `List<String>` depends on the `List` interface contract rather than a specific implementation such as `ArrayList`.

This allows callers to provide different `List` implementations and reduces coupling.

The limitation is that the method can directly rely only on operations defined by the `List` contract. A more specific parameter type is appropriate only when the method genuinely requires implementation-specific behavior.

## JOIN Cardinality

JOIN returns one result row for each pair of rows that satisfies the join condition.

A one-to-many or many-to-many relationship can therefore produce multiple rows for one original row.

A structured diagnosis should:

1. Confirm the expected relationship cardinality.
2. Check whether join keys are unique on the expected side.
3. Find duplicate key values.
4. Verify that the join condition contains the correct columns.
5. Distinguish valid one-to-many results from duplicate data or an incorrect join condition.

`GROUP BY`, `DISTINCT`, or additional filters should not be used merely to hide an unexplained row multiplication problem.

## Transaction Recovery

`replaceAllTasks()` is one atomic business operation, so the Service owns the complete transaction boundary.

When an INSERT fails, the Service should:

1. Roll back the transaction.
2. Restore the Connection's original auto-commit state.
3. Preserve and rethrow the primary exception.
4. Attach rollback or restoration failures as suppressed exceptions.
5. Leave a caller-owned Connection open.

The original state must be restored rather than assuming that auto-commit was always initially enabled.

## HTTP Headers and Error Responsibilities

`Content-Type` describes the media type of the current message body.

`Accept` is a request header that describes the response media types acceptable to the client.

When valid JSON reaches the database but the operation fails:

- The Service performs transaction recovery and preserves the primary failure.
- The Controller maps the internal failure to a fixed and safe HTTP status and public response body.
- Server-side diagnostics retain protected technical details such as exception type, message, stack trace, and request context.

A public `500` response must not expose raw `SQLException` details because they may reveal SQL, schema, constraints, or internal implementation information.

The Day 21 simulation provided correct evidence for the response direction of `Accept`. This is evidence for moving the topic from Weak toward Review, subject to Question Bank update validation.

## Outcome Evidence and Invocation Evidence

An observer Connection verifies the final database state visible from another Connection.

It can provide evidence that:

- A complete replacement became externally visible after success.
- The original committed state remained visible after failure.
- No partial replacement state was exposed.

It cannot directly prove that `commit()`, `rollback()`, or `setAutoCommit()` was invoked.

Direct invocation evidence requires a mock, spy, wrapper, or instrumented Connection.

## Evidence-Based Coverage Decision

Test sufficiency must be evaluated using:

- Current requirements
- Important risks
- Relevant failure modes
- Acceptance evidence

The number of tests alone does not prove sufficient coverage because many tests may repeat the same behavior while leaving an important risk untested.

Day 21 found:

| Check | Result |
|---|---|
| Reproducible production defect | None |
| Existing requirement without acceptance evidence | None |
| User-visible behavior gap | None |
| New production-code issue | None |
| Need for an additional test | No |
| Need for production-code modification | No |
| Decision | Retain the 38-test baseline |

The Day 21 weaknesses were interview-analysis and expression weaknesses, not task-manager behavior gaps.

## Five-Minute Presentation and Self-Introduction

The selected narrative is:

> Research to engineering

The presentation should show that completing the doctoral qualification examination was an important milestone, after which the long-term career direction was actively reassessed.

The transition is explained using the current context:

- Completion of the doctoral qualification examination
- Advisor retirement
- No remaining course pressure
- Desire to gain real engineering, teamwork, and system-maintenance experience
- Intention to build a sustainable long-term career

The motivation should not sound like escaping a research difficulty. It should be supported by concrete engineering preparation and a clear intention to contribute.

The planned five-slide structure is:

1. Personal positioning
2. Research capabilities
3. Active transition from research to engineering
4. Engineering and AI-assisted development evidence
5. Chunghwa Telecom role fit and closing

The core message is:

> Research training provides systematic problem analysis, while recent Java, testing, GitHub, CI, and AI-assisted development practice demonstrates an active transition toward responsible engineering work.

Three timed readings were completed:

| Reading | Time |
|---|---:|
| First reading | 06:55 |
| Second reading | 05:19 |
| Third reading | 04:25 |

The third version forms an 80-percent content baseline. The remaining content and layout should be adapted after the official interview notice provides the required template, page count, topics, and time limit.

The confirmed laboratory name is `Logos Lab`.

## Interviewer Q&A Simulation

A six-question interviewer Q&A simulation covered:

- The reason for pausing doctoral study and applying to Chunghwa Telecom
- Evidence that the career transition is not an escape from difficulty
- Handling an unfamiliar legacy system under a two-day deadline
- AI usage boundaries for confidential enterprise systems
- `Content-Type`, `Accept`, and error responsibility boundaries
- Evidence-based testing and coverage decisions

All six questions were answered without coaching during the attempt. Corrected answers were preserved afterward.

The simulation confirmed stable or improving understanding of HTTP headers, transaction recovery, responsibility boundaries, outcome evidence, and coverage decisions.

It also revealed a new communication weakness: answers about unfamiliar systems relied on ChatGPT too early and did not first demonstrate the learner's own investigation method.

## Unfamiliar-System Investigation

For an unfamiliar system with incomplete documentation and a short deadline, the investigation order should be:

1. Confirm impact, deadline, constraints, and acceptable risk.
2. Examine documentation, code, logs, monitoring data, and recent changes.
3. Build a minimum reproducible case.
4. Rank possible causes using evidence.
5. Validate in an isolated or test environment.
6. Prefer reversible changes and avoid uncontrolled production modifications.
7. Report confirmed facts, remaining uncertainty, risks, next steps, and expected time.

The minimum answer structure is:

> Personal analysis → system evidence → reproduction → risk control → progress report → optional AI assistance

## AI-Assisted Engineering

AI can improve the efficiency of information organization, problem exploration, and solution comparison.

It is not a necessary condition for completing engineering work and cannot guarantee that its output is correct.

The engineer must:

- Follow company security and AI-use policies.
- Avoid providing customer data, credentials, source code, internal architecture, logs, or other sensitive information to unapproved external services.
- Use abstracted problems or synthetic test data only when policy permits.
- Validate AI-assisted results through system evidence, tests, CI, documentation, and human judgment.
- Retain a complete non-AI investigation method when external AI is prohibited.

ChatGPT may be named as a concrete tool, but the explanation should emphasize how it was used, how its output was verified, and how responsibility remained with the engineer.

## Learner Reflection

### Core outcomes

Day 21 completed the timed mock written test, established a time-bounded written-answer strategy, planned the five-minute presentation and self-introduction, completed the interviewer Q&A simulation, and retained the 38-test baseline after finding no verified implementation or coverage gap.

### Most important mock-test finding

The mock test achieved 76／100 with no unanswered questions.

The adopted strategy is to write the minimum complete answer first and add reasons or project evidence only when time remains.

### Presentation progress

The five-minute presentation uses the research-to-engineering narrative.

The third reading completed in 04:25, and the current script is an 80-percent baseline while waiting for the official presentation format.

### New Q&A weakness

The unfamiliar-system answer placed ChatGPT before the learner's own analysis.

Future answers must first demonstrate evidence collection, problem reproduction, risk control, and reporting. They must also explain how the same work would be completed if external AI were prohibited.

### Stable concepts

- `Accept` as the response media types acceptable to the client
- Outcome evidence versus invocation evidence
- Service, Controller, and server-side diagnostics responsibilities
- Coverage decisions based on evidence rather than test count
- A positive and action-supported explanation of the research-to-engineering transition

### Continued Weak／Review areas

- Static and instance compile-time boundary
- JOIN cardinality and row multiplication
- `List` interface contract, substitutability, and limitations
- Complete transaction-recovery flow
- Requirement-, risk-, failure-mode-, and evidence-based coverage decisions
- Unfamiliar-system investigation without beginning from AI
- AI security boundaries and non-AI fallback
- Cross-topic retrieval of older material

### Next priorities

The next review stage should first sample Weak／Review questions across earlier topics.

A new mock test should be attempted after the Question Bank has moved closer to Review／Stable rather than immediately repeating another full simulation.

Presentation and script work should continue, with the goal of producing a presentable deck by 2026-09-12 and using any remaining time for refinement.

## Time Summary

Day 21 spans 2026-09-07 and 2026-09-08.

Recorded working intervals through Reflection:

- 2026-09-07 09:17:16–21:50:39 CST
- 2026-09-08 09:47:28–10:47:02 CST

Excluded time:

| Exclusion | Time |
|---|---:|
| Lunch | 02:36:20 |
| Dinner | 00:22:58 |
| Searching for the graduation certificate | 02:00:00 estimated |

Effective training time through Reflection:

> 08:33:39

This calculation includes reading, thinking, information digestion, answering, feedback, revision, and stage-gate interaction. It excludes meals, overnight time, and external errands.

The graduation-certificate errand did not have an exact start and completion timestamp, so the mutually accepted conservative estimate of 02:00:00 is used transparently.

## Workflow Corrections

The initial time summary counted only directly timed activities and therefore understated effective training time.

The corrected method uses the overall working intervals and excludes recorded meals, overnight time, and external errands. Reading, thinking, feedback, revision, and interaction time remain part of effective training.

An assistant response generated during the first D21-07 attempt contained unrelated multilingual fragments and draft text. That response was discarded in full, created no repository change, and was excluded from training evidence. D21-07 was restarted cleanly on 2026-09-08.

These corrections apply the existing timing and evidence rules and do not modify the Command Registry.

## Delivery

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 21 Notion record.
