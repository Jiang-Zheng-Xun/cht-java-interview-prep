# Day 28 — Testing and DI Cross-day Validation and Application Closure

## Date

2026-09-14

## Original Objectives

- Validate the Testing evidence boundary across days, without hints, and under a changed scenario.
- Validate the dependency-injection abstraction boundary and composition-root ownership across days.
- Preserve representative breadth through one controlled Interview Review.
- Reinforce no more than two evidence-based primary gaps.
- Maintain Slide 4 presentation fluency without unnecessary revisions.

## Continuity and Baseline

The current source of truth remained CHT Interview Command Registry v1.5, effective 2026-09-08.

Day 27 had completed delivery closure with a final effective time of `05:54:23`.

Question Bank verification at the start of Day 28 showed:

- Weak: 64
- Review: 59
- Stable: 5
- Total: 128
- Missing required fields: 0

Git and Maven baseline:

- Base branch: `develop`
- Local HEAD: `9f62924`
- Remote `origin/develop`: `9f62924`
- Ahead / behind: `0 / 0`
- Working tree: clean
- Maven tests: 38
- Failures / Errors / Skipped: `0 / 0 / 0`
- Build result: `BUILD SUCCESS`
- `task-manager/pom.xml`, source layout, and test layout were verified
- Day 27 local and remote feature branches were absent
- Day 28 local and remote feature branches were absent before local branch creation
- No concrete, reproducible, and meaningful implementation or coverage gap was identified
- The Java Task Manager remained at its verified 38-test baseline

Day 28 used the local feature branch:

`feature/day28-testing-di-cross-day-presentation-maintenance`

The branch was intentionally not pushed before the required delivery stage.

## Controlled Interview Review

The Interview Review remained one learning block.

- Overall time: `09:32:13–10:27:07 CST`
- Overall effective time: `00:54:54`
- Learner-answer questions: 5
- Pure answer time: `00:34:43`
- External references or hints: none

### Testing Evidence Boundary

The changed scenario involved two account updates in one transaction. The second update failed, `rollback()` was invoked, `commit()` was not invoked, and rollback itself produced a suppressed exception.

The learner correctly distinguished:

- Mock or spy verification as interaction and invocation evidence
- Primary and suppressed exceptions as internal failure-path evidence
- An independent observer connection as externally visible final-state evidence

The learner correctly explained that rollback invocation does not prove rollback success and cannot independently prove that no partial update remains.

Decision:

- Testing evidence boundary retained `Review`
- Cross-day, unprompted, changed-scenario validation passed
- The topic became a future `Stable` candidate, but was not upgraded from one cross-day result

### Dependency-injection Boundary

The changed scenario used a Controller that directly constructed `SQLiteTaskRepository` and `TaskService`.

The learner correctly stated that:

- `TaskController` should depend on a service abstraction
- `TaskService` should depend on a repository abstraction
- Constructor injection reduces concrete coupling and improves testability
- The production composition root creates concrete objects and connects the object graph

The corrected complete wiring is:

- The composition root creates `SQLiteTaskRepository`
- The repository abstraction is injected into `TaskService`
- The service abstraction is injected into `TaskController`
- A Controller unit test injects a fake or mock service without connecting to SQLite
- The Controller constructor receives and stores its dependency rather than constructing concrete implementations

Decision:

- Constructor injection and the DI abstraction boundary advanced from `Weak` to `Review`
- The upgrade used cross-day, unprompted, changed-scenario first-answer evidence

### SQL WHERE and HAVING

The learner correctly knew that:

- `WHERE` filters individual rows before grouping
- `HAVING` filters groups after aggregation

The first SQL implementation remained incorrect because it used an invalid `WHERE` expression instead of `HAVING`.

During reinforcement, the learner correctly placed:

- `status = 'COMPLETED'` in `WHERE`
- `COUNT(*) >= 3` in `HAVING`

Two SQL syntax details still required correction:

- SQL equality uses `=` rather than `==`
- String values require quotes, such as `'COMPLETED'`

Decision:

- WHERE and HAVING remained `Weak`
- The same-day correction did not justify a Mastery upgrade
- Future validation should use a cross-day or changed aggregation scenario

### Git and CI

The learner correctly explained that a CI compilation failure cannot be ignored even when all local tests pass.

The answer included:

- Reading the CI log
- Confirming whether the failure is reproducible
- Comparing Java versions and environments
- Checking code, dependencies, paths, and runner configuration
- Re-running CI after correction
- Recording the root cause for future diagnosis

The corrected answer added that the intended Java-version contract must be verified before deciding whether to upgrade CI to Java 21 or preserve Java 17 compatibility.

Decision:

- Git and CI retained `Review`
- The answer provided additional reliable changed-scenario evidence

### ReDoS

The learner correctly connected nested ambiguous quantifiers and near-miss long input with excessive backtracking.

The answer covered:

- CPU consumption
- Latency
- Worker-thread occupation
- Service availability risk
- Regex simplification
- Input-length limits
- Timeout
- Non-backtracking engines
- Rate limiting
- CPU, thread, and traffic evidence

The corrected answer added comparisons of latency growth across input sizes, timeout behavior, concurrent malicious input, and normal-input correctness.

Decision:

- ReDoS retained `Review`
- Measurable-evidence wording improved compared with Day 27

## Four-dimension Evidence

### Concept

Testing and DI boundaries were correct in the first cross-day answers. WHERE and HAVING were understood conceptually but not transferred reliably into SQL syntax.

### Wording

Testing, DI, Git/CI, and ReDoS answers contained the main causal relationships. DI production and test wiring benefited from one additional concrete object-graph explanation.

### Scenario Application

Testing and DI transferred successfully to changed scenarios. SQL aggregation filtering remained unreliable during the first implementation.

### English Reading

The learner showed no primary difficulty with observer, suppressed exception, constructor injection, composition root, CI, backtracking, or aggregation terminology.

## Mastery Decisions

| Topic | Day 28 decision | Evidence |
| --- | --- | --- |
| Testing evidence boundary | Review retained; Stable candidate | Cross-day, unprompted, changed-scenario first answer was correct |
| Constructor injection / DI boundary | Weak to Review | Cross-day first answer correctly identified abstraction direction and composition-root ownership |
| WHERE / HAVING | Weak retained | Concept was known, but the first SQL implementation and reinforced syntax remained inaccurate |
| Git / CI | Review retained | Changed-scenario answer correctly rejected ignoring CI and described systematic diagnosis |
| ReDoS | Review retained | Cause, mitigation, and measurable operational evidence were mostly correct |
| Transaction failure handling | Strong Review / Stable candidate retained | Not retested because Day 27 evidence remained strong |
| Generics | Review retained | Not retested because Day 27 had already supplied cross-day timed evidence |
| Environment | Appendix / low priority | Did not occupy a primary review slot |

Same-day correction alone did not cause a Mastery upgrade.

## Formal Application Result and Scope Change

At `2026-09-14 10:22:00 CST`, the learner received the formal Chunghwa Telecom application result.

Result:

- The application did not pass the document-screening stage
- The expected first-round written examination, presentation, and interview would not take place for this application

The formal result invalidated the remaining interview-specific assumptions.

The Day 28 scope was therefore reduced:

- No additional Interview Review questions were added
- Slide 4 rehearsal was cancelled
- Presentation v0.3 was not modified
- The canonical third-version script was not modified
- No presentation v0.4 or fourth-version script was created

`cht-interview-day23-presentation-v0.3.pptx` and the canonical third-version script remain preserved artifacts that may be adapted if a future application requires a similar presentation.

## Learner Reflection

The learner confirmed that the most important technical improvement was the DI and constructor-injection boundary advancing from Weak to Review.

The remaining SQL weakness was that the learner knew the WHERE / GROUP BY / HAVING order but did not produce correct SQL on the first attempt. The reinforcement fixed the logical placement, while equality and string-literal syntax still required attention.

The application result was recorded as:

- Did not pass document screening

The next immediate job-search action is to review and rebuild:

- The one-page resume
- The 104 profile and platform resume
- The positioning and evidence used to present the learner's current capabilities

The 28 days of training should be translated into concise evidence, including:

- Java layered design
- JDBC and SQLite
- Transaction rollback
- HTTP API and safe error handling
- Integration testing and externally visible state verification
- GitHub Issue, feature branch, Pull Request, CI, and merge workflow
- A stable 38-test baseline

The Java Task Manager's next goal is to complete useful CRUD and database-management capabilities while eliminating known concrete, reproducible, and meaningful testing coverage gaps.

After the Java project reaches a credible portfolio state, the learner plans to review the private graduate-school blockchain project, trace the code, reconstruct specifications and documentation, and extract representative features such as Merkle Patricia Trie and blockchain transactions into independently reproducible and safely publishable GitHub projects.

Private research code must be reviewed for ownership, confidentiality, credentials, third-party code, and publication boundaries before extraction.

## Career-strategy Handoff

The next work phase should proceed in parallel rather than waiting for every project to become complete.

### Immediate Track

- Rebuild the one-page resume
- Update the 104 profile
- Review whether the current career positioning matches the evidence
- Translate training results into employer-relevant outcomes
- Resume job applications while portfolio work continues

### Java Portfolio Track

- Complete practical CRUD behavior
- Review API semantics and validation
- Preserve Controller, Service, and Repository responsibility boundaries
- Strengthen transaction and error-handling behavior
- Audit tests by meaningful risk and externally observable behavior
- Document setup, architecture, API usage, tests, and CI

### Blockchain Portfolio Track

- Trace and understand the existing private code
- Reconstruct specifications and architecture documentation
- Identify independently reproducible representative components
- Add focused automated tests
- Publish only sanitized, owned, and clearly documented extracts

### Longer-term Security Track

- Explore Ghidra and reverse-engineering fundamentals through free resources
- Build foundations in assembly, binary formats, debugging, and static analysis
- Evaluate certifications later based on actual job-market relevance, cost, and available study capacity

Target roles should be reviewed across Java backend, software engineering, security engineering, application security, systems or C/C++ development, research engineering, and technically grounded project or system-analysis work.

## Time Record Before Daily Log

- Continuous effective work before lunch: `09:14:03–12:50:22` — `03:36:19`
- Lunch: `12:50:22–16:35:44` — excluded `03:45:22`
- Short closure and Reflection: `16:35:44–17:07:37` — `00:31:53`
- Effective subtotal before Daily Log: `04:08:12`

The effective time includes reading, answering, feedback, stage transitions, response to the formal result, career-strategy discussion, and learner-led Reflection. Lunch was excluded.

## Final Direction

Day 28 ends the interview-specific daily training sequence for this Chunghwa Telecom application.

The next chat should begin a new work phase focused on:

1. Resume and 104 profile revision
2. Career-direction reassessment
3. Java Task Manager portfolio completion
4. Safe extraction of representative blockchain components
5. Practical reverse-engineering learning when capacity permits

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 28 Notion record.
