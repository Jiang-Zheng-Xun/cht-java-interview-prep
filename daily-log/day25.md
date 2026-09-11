# Day 25 — Cross-topic Validation, OOP Verification, and Dual-mode Presentation Rehearsal

- Date: 2026-09-11
- Project key: `cht-interview`
- Issue: #52 — Day 25: Complete cross-topic validation and compare two presentation rehearsal modes
- Presentation baseline: `cht-interview-day23-presentation-v0.3.pptx`
- Script baseline: canonical third-version script
- Final delivery evidence is tracked by linked GitHub Issue, Pull Request, GitHub Actions, and canonical Day25 Notion record.

## Objectives

- Validate retained understanding across multiple interview topics using five learner-first questions.
- Perform cross-day, unprompted validation of inheritance/composition, generics, and static/instance concepts.
- Reinforce no more than two major gaps.
- Compare a scripted rehearsal with a slide-anchor-only rehearsal.
- Decide whether presentation or script revision is justified by evidence.
- Preserve a delivery-stable Day 25 handoff for Day 26.

## Baseline Validation

- Current branch before setup: `develop`
- Local `develop` HEAD: `0e29a8a`
- `origin/develop` HEAD: `0e29a8a`
- Ahead/behind: `0/0`
- Working tree: clean
- Local branches before Day 25 setup: `develop`, `main`
- Remote branches before Day 25 setup: `origin/develop`, `origin/main`
- Tests run: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: `BUILD SUCCESS`
- No concrete, reproducible, meaningful implementation or coverage gap was found.
- The task-manager repository therefore remained at the 38-test baseline without implementation changes.

## Delivery Setup

- Issue: #52
- Branch: `feature/day25-cross-topic-oop-dual-rehearsal`
- Branch starting HEAD: `0e29a8a`
- Upstream: `origin/feature/day25-cross-topic-oop-dual-rehearsal`
- Branch creation and push completed before modifying files.
- The working tree remained clean after branch setup.

## Five Learner-first Questions

### Q1 — Inheritance, Composition, and Replaceable Behavior

The answer correctly interpreted `ReportService extends PdfExporter` as “ReportService is a PdfExporter” and recognized that this relationship was inappropriate when the purpose was only to reuse `validate()`.

The answer also correctly selected composition because:

- `ReportService` is not conceptually a subtype of `PdfExporter`.
- PDF export is only one component or replaceable capability of the reporting service.
- Future CSV and JSON support requires substitutable implementations rather than inheritance from one concrete exporter.

The proposed abstraction initially used `ReportService` as the interface name. A responsibility-oriented abstraction such as `ReportExporter` would express the replaceable behavior more accurately.

Evidence supports promotion from Weak to Review, but not Stable, because additional scenario-based retrieval is still needed.

### Q2 — SQL JOIN Cardinality

The answer recognized that a join returns rows for matching pairs and that an unmatched right-side value in a `LEFT JOIN` becomes `NULL`.

However, it did not compute the complete row cardinality. The expected result contained four rows:

- Amy: two matching rows
- Ben: one matching row
- Cara: one unmatched left-side row with right-side `NULL`

The missing matching-pair count shows that JOIN cardinality remains Weak.

### Q3 — Generic Class and Generic Method

The answer correctly identified that `Cache<T>` should be a generic class because the object retains values of type `T` throughout its lifetime.

The generic method declaration was incorrect:

`public static <Integer> T last(List<T> values)`

`<Integer>` declares a new type parameter literally named `Integer`; it does not declare `T`. The method should declare its own type parameter before the return type:

`public static <T> T last(List<T> values)`

The distinction between generic-class scope and generic-method scope was understood after reinforcement, but independent declaration accuracy remains Weak.

### Q4 — Static and Instance State

The answer correctly changed `recordRequest()` into an instance method:

`public void recordRequest()`

It also correctly connected:

- the caller holding reference `first`;
- `first` pointing to the receiver object;
- `this` referring to that receiver during the call;
- the method updating the receiver’s own `requestCount` state.

The original program’s output should be stated explicitly as `0` and `0`, rather than only as default values.

Evidence supports promotion from Weak to Review, but another independent scenario is required before considering Stable.

### Q5 — Runtime Polymorphism

The answer correctly explained that:

- the reference type controls which members are available at compile time;
- the actual object type selects the overridden implementation during runtime dispatch;
- `format()` executes the `UpperCaseFormatter` implementation and converts `"java"` to uppercase.

`formatter.reset()` does not compile because the declared reference type `Formatter` does not expose `reset()`. This is distinct from whether the implementation class overrides an interface method.

The answer also needs a sharper separation between:

- constructor execution during object creation;
- reference assignment;
- method invocation;
- dynamic dispatch during an overridable instance-method call.

Runtime polymorphism therefore remains Review.

## Question Timing

| Question | Pure answer time |
|---|---:|
| Q1 | 00:06:37 |
| Q2 | 00:04:44 |
| Q3 | 00:08:29 |
| Q4 | 00:09:07 |
| Q5 | 00:13:55 |
| Total | 00:42:52 |

The complete question stage, including feedback and learner digestion, ran from `09:01:07` to `10:03:12` and contributed `01:02:05` of effective time.

## Four-dimensional Evidence

### Concept

- Correctly identified the direction of an is-a relationship.
- Correctly distinguished inheritance from composition based on responsibility and replaceability.
- Correctly connected caller, reference, receiver, `this`, and object state.
- Correctly distinguished reference type from actual object type.
- Understood generic-class and generic-method scope after reinforcement.
- JOIN matching-pair cardinality and exact generic-method declaration remain incomplete.

### Wording

- Explained OOP decisions using is-a, composition, coupling, receiver, and dynamic dispatch.
- Some wording still mixed object creation, assignment, and dispatch.
- Interface naming should describe the replaceable responsibility rather than the consuming service.

### Scenario Application

- Correctly applied composition to future PDF, CSV, and JSON implementations.
- Correctly repaired an instance-state update scenario.
- Correctly predicted overridden formatter behavior.
- Did not fully enumerate the rows produced by a one-to-many `LEFT JOIN`.

### English Reading

- Correctly interpreted terms including `extends`, `reference type`, `actual object type`, `receiver`, `runtime polymorphism`, `generic class`, and `generic method`.
- Declaration syntax and execution-phase terminology require additional retrieval practice.

## Reinforcement

Two major gaps were reinforced:

1. SQL JOIN matching-pair cardinality and unmatched-row behavior.
2. Generic-class versus generic-method declaration scope, including why `<Integer> T` is invalid for the intended method.

Reinforcement ran from `10:22:49` to `10:43:57` and contributed `00:21:08` of effective time.

Both topics remain Weak because same-day reinforcement does not provide cross-day mastery evidence.

## Mastery Decisions

| Topic | Before | Day 25 decision | Evidence |
|---|---|---|---|
| Inheritance/composition | Weak | Review | Correct unprompted is-a direction and composition decision in a changed scenario |
| Static/instance | Weak | Review | Correct unprompted repair and caller/reference/receiver/`this` explanation |
| Generics | Weak | Weak | Generic class concept understood, but generic method declaration was incorrect |
| JOIN cardinality | Weak | Weak | Matching and `NULL` concepts recognized, but complete row count was missing |
| Runtime polymorphism | Review | Review | Core dispatch model correct, but `reset()` and execution-phase explanations were incomplete |

No topic was promoted to Stable.

## Scripted Rehearsal

- Presentation version: v0.3
- Script version: canonical third version
- Complete uninterrupted rehearsal: yes
- Total time: `04:35`
- Omitted content: none
- Unnecessary expansion: none

| Slide | Time |
|---|---:|
| 1 | 00:31 |
| 2 | 00:54 |
| 3 | 00:45 |
| 4 | 01:28 |
| 5 | 00:57 |
| Total | 04:35 |

Observations:

- Slide 1 to Slide 2 transition was acceptable.
- The Slide 2 research-method transition was acceptable.
- Slide 4 organization was acceptable when supported by the script.
- The sentence about entering an environment with large information systems, real service scenarios, and long-term maintenance needs caused a brief hesitation.
- No PPTX or script changes were made.

The complete stage ran from `10:46:47` to `11:14:58` and contributed `00:28:11` of effective time.

## Slide-anchor-only Rehearsal

The first Slide 1 attempt was restarted. The recorded evidence below uses the complete five-slide take from `14:39:09` to `14:44:49`.

- Presentation version: v0.3
- Script viewed during the take: no
- Total time: `05:40`
- Estimated canonical-script recall: approximately 60–70%

| Slide | Time |
|---|---:|
| 1 | 00:44 |
| 2 | 01:10 |
| 3 | 00:48 |
| 4 | 01:43 |
| 5 | 01:15 |
| Total | 05:40 |

Observations:

- Slide 1’s transition from systems research toward information-systems engineering practice was not yet natural.
- Slide 2’s research-method transition was acceptable.
- Slide 4 omitted the GitHub Issue, feature branch, Pull Request, CI, and merge-to-develop delivery flow.
- Slide 5 had a wording-retrieval hesitation near the AI-tool capability statement.
- No clear unnecessary expansion was identified.
- No PPTX or script changes were made.

The complete stage ran from `14:35:53` to `15:09:29` and contributed `00:33:36` of effective time.

## Dual-mode Comparison

| Dimension | Scripted | Slide-anchor only | Difference |
|---|---:|---:|---:|
| Total | 04:35 | 05:40 | +01:05 |
| Slide 1 | 00:31 | 00:44 | +00:13 |
| Slide 2 | 00:54 | 01:10 | +00:16 |
| Slide 3 | 00:45 | 00:48 | +00:03 |
| Slide 4 | 01:28 | 01:43 | +00:15 |
| Slide 5 | 00:57 | 01:15 | +00:18 |

The scripted mode was complete and concise. The slide-anchor-only mode was slower and exposed retrieval and organization gaps, especially:

- Slide 1 positioning and transition;
- Slide 4 GitHub delivery-flow completeness;
- Slide 5 AI-tool wording.

The evidence indicates a familiarity and retrieval-order problem rather than a defect in the five-slide narrative.

## Version Decision

- Keep `cht-interview-day23-presentation-v0.3.pptx`.
- Do not create v0.4.
- Keep the canonical third-version script.
- Do not create a fourth-version script.
- Do not modify the PPTX or canonical script on Day 25.
- Preserve the Slide 4 GitHub delivery flow as a future rehearsal cue.
- Improve Slides 1 and 5 through oral familiarity and retrieval practice.
- No concrete, reproducible content problem justified a versioned revision.

The physical PPTX verification confirmed:

- five slides;
- the three Slide 2 evidence labels are 12.75 pt and bold;
- Slide 4 and its notes already contain GitHub and CI evidence.

The version-decision stage ran from `17:14:21` to `17:21:48` and contributed `00:07:27` of effective time.

## Reflection

### Core Outcomes

- Correctly determined the direction of `A extends B` as “A is a B.”
- Correctly selected composition when inheritance would only reuse a method and future implementations must be replaceable.
- Correctly connected caller, reference, receiver, `this`, and instance state.
- Correctly explained the central role of actual object type in runtime dispatch.
- Completed the scripted rehearsal in `04:35` without omissions or unnecessary expansion.

### Most Important Technical Understanding

- Inheritance/composition decision criteria.
- Static versus instance state and receiver semantics.
- Generic-class versus generic-method scope.
- Reference type versus actual object type.

### Most Visible Technical Weaknesses

- Exact generic-method declaration syntax.
- JOIN cardinality.
- Clear separation of constructor execution, assignment, method invocation, and dynamic dispatch.

### Presentation Learning

The `04:35` scripted rehearsal and `05:40` slide-anchor rehearsal show that the main presentation issue is unscripted retrieval and organization. The existing narrative remains suitable.

Keeping v0.3 and the canonical third-version script was therefore appropriate because the observed issues do not establish a content-level defect.

### Workload Decision

Day 25’s five-question breadth, difficulty, answer time, and two-gap reinforcement limit were appropriate.

For Day 26, trial six learner-first questions with:

- a balanced mix of Weak, Review, overdue, and controlled-random topics;
- one core concept per question;
- approximately four minutes of reasonable answering time per question;
- no excessive subdivision into many subquestions.

If the extra question disrupts pacing or depth, return to five questions.

## Coverage and Day 26 Handoff

Representative coverage across recent training includes:

- Java/OOP
- DI/Design
- Git/CI
- Security
- SQL
- JDBC/transaction
- HTTP/REST
- Testing

The Question Bank contains 126 questions before the Day 25 evidence update:

- Weak: 67
- Review: 53
- Stable: 6
- Duplicate questions: 0

Day 25 evidence supports the following projected update:

- Weak: 65
- Review: 55
- Stable: 6
- Total: 126

This projection depends only on promoting inheritance/composition and static/instance from Weak to Review.

Topics with no recent review since Day 21 include:

- Algorithms
- Date Time
- Environment
- Functional Programming

Day 26’s six-question candidate allocation is:

1. Generics — Weak cross-day validation.
2. JOIN cardinality — Weak cross-day validation.
3. One overdue capability.
4. A second overdue capability.
5. One Review retention check.
6. One controlled-random question from another major capability.

Formal questions must not be disclosed before the Day 26 learner-first stage.

Day 26 should also continue presentation rehearsal with the following cues:

- make the Slide 1 positioning transition natural;
- retain the complete GitHub delivery flow on Slide 4;
- retrieve the AI-tool capability statement naturally on Slide 5;
- keep v0.3 and the canonical third-version script unless new reproducible evidence justifies a versioned revision.

## Effective-time Record

| Activity | Effective time |
|---|---:|
| D25-01 baseline | 00:05:54 |
| Branch setup | 00:02:25 |
| D25-02 five questions and feedback | 01:02:05 |
| D25-03 reinforcement | 00:21:08 |
| D25-04 scripted rehearsal | 00:28:11 |
| D25-05 slide-anchor rehearsal | 00:33:36 |
| D25-06 version decision | 00:07:27 |
| D25-07 reflection and artifact verification | 00:29:35 |
| Effective time before D25-08 | 03:10:21 |

Excluded time:

- Lunch: `11:14:58–14:35:02` (`03:20:04`)
- ChatGPT abnormal response/wait interval: `15:09:29–17:07:57` (`01:58:28`)
- Dinner: `17:53:45–18:48:10` (`00:54:25`)

D25-08 closure time is added only after its stage gate is verified.

## Artifact Verification Before Closure

- Current branch: `feature/day25-cross-topic-oop-dual-rehearsal`
- HEAD before creating this log: `0e29a8a`
- Working tree: clean
- `git diff --check`: no output
- `git diff --stat`: no output
- task-manager: no diff
- PPTX: unchanged
- Canonical script: unchanged
- No new implementation or coverage gap was found.
