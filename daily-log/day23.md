# Day 23｜受控跨主題複習與簡報初稿

## Date

2026-09-09

## Goal

Complete a five-question controlled cross-topic review, reinforce no more than two major gaps, and evolve the canonical third-version presentation script into a usable five-slide first draft.

Retain the existing task-manager and 38-test baseline unless a concrete, reproducible, and meaningful implementation or coverage gap is verified.

## Baseline

Day 23 started from the following verified baseline:

- Branch: `develop`
- Local HEAD: `26640ee`
- Remote `origin/develop`: `26640ee`
- Ahead／behind: `0 / 0`
- Working tree: clean
- Local Day 22 branch: absent
- Remote Day 22 branch: absent
- Local Day 23 branch before setup: absent
- Remote Day 23 branch before setup: absent
- Maven tests: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: `BUILD SUCCESS`

The Day 23 feature branch was created from `26640ee`:

`feature/day23-controlled-review-presentation-draft`

The branch tracks its same-named remote branch.

VS Code displayed a GitHub Copilot modernization prompt suggesting migration from Java 21 to the latest LTS version. `Not Now` was selected.

No extension, Java runtime, Maven configuration, CI configuration, dependency, source-code, or repository change was made.

## Controlled Cross-topic Review

Day 23 reviewed five questions, one at a time:

1. Git／CI failure handling
2. JDBC transaction failure responsibility
3. Java static method and instance state
4. ReDoS
5. Database outcome evidence versus method-invocation evidence

### Answer Timing

| Question | Topic | Answer time |
|---|---|---:|
| Q1 | Git／CI failure handling | 00:02:51 |
| Q2 | JDBC transaction failure responsibility | 00:05:11 |
| Q3 | Static method and instance state | 00:04:19 |
| Q4 | ReDoS | 00:05:11 |
| Q5 | Outcome versus invocation evidence | 00:02:29 |
| Total | — | 00:20:01 |

The overall review block included reading, answering, feedback, corrected answers, information digestion, and transitions.

## Review Evidence

### Q1 — Git／CI Failure Handling

The learner correctly rejected treating an unexpected CI failure as complete or harmless.

A failed CI result requires investigation of the error information, identification of its cause, correction where necessary, and confirmation that the expected validation result is restored.

Evidence:

- Concept: correctly identified that a CI failure requires investigation
- Wording: concise and understandable
- Scenario application: connected the failure to comparison of expected validation results
- English reading: understood the CI scenario and completion boundary

Mastery evidence: Review supporting evidence.

### Q2 — JDBC Transaction Failure Responsibility

The learner correctly identified the second `INSERT` failure as the primary failure, but initially treated the absence of a rollback call as a rollback failure.

The corrected distinction is:

- No rollback call: transaction-flow or responsibility defect
- Rollback called and throws `SQLException`: rollback failure
- Auto-commit restoration succeeds: no restoration failure
- Restoring auto-commit before a required rollback can expose partial-update risk
- If a primary failure already exists, rollback or restoration failures should be preserved as suppressed exceptions

Evidence:

- Concept: initial confusion between missing rollback and rollback failure
- Wording: could describe the observed sequence but assigned one event to the wrong failure category
- Scenario application: identified insert risk but initially prioritized rollback failure incorrectly
- English reading: understood primary, suppressed, and restoration terminology, but misclassified the missing invocation

Mastery evidence remains Weak. The corrected explanation occurred after feedback and requires cross-day, unprompted, changed-scenario validation.

### Q3 — Static Method and Instance State

The learner correctly determined that the code would fail to compile because a static method has no specific instance or `this` and cannot directly access the instance field.

The learner correctly proposed changing `increment()` to an instance method, but initially described the call as:

`SessionCounter.increment()`

The instance method must instead be called through a specific object:

`counter.increment()`

Evidence:

- Concept: correctly identified the static and instance conflict
- Wording: clearly mentioned the lack of `this`
- Scenario application: selected the correct instance-method design but used class-style invocation
- English reading: understood the class and method terminology

Mastery evidence remains Weak and is close to Review. Cross-day changed-wording or changed-scenario evidence is still required.

### Q4 — ReDoS

The learner correctly identified ReDoS and explained that a long attacker-controlled input can consume regex-engine resources, occupy processing threads, and reduce service availability.

The learner proposed relevant controls:

- Avoid ambiguous nested regex structures
- Limit input length
- Apply a timeout where available
- Prefer a non-backtracking engine where appropriate

The explanation could be improved by explicitly stating that overlapping nested quantifiers cause a backtracking engine to explore many possible groupings before rejecting the input.

Evidence:

- Concept: correct
- Wording: direct and understandable
- Scenario application: connected the input to resource exhaustion and service impact
- English reading: correctly interpreted ReDoS, regex engine, backtracking, and timeout

Mastery evidence: Review candidate.

### Q5 — Outcome Evidence versus Invocation Evidence

The learner correctly explained that an observer connection can prove the final database state visible to another connection.

Observer evidence cannot directly prove whether `commit()` or `rollback()` was called.

A mock or spy is required when the test specifically needs method-invocation evidence.

Evidence:

- Concept: correct distinction between state and interaction evidence
- Wording: concise and precise
- Scenario application: selected observer evidence for outcome and mock／spy for invocation
- English reading: correctly interpreted observer evidence and invocation terminology

Mastery evidence: Stable supporting evidence.

## Reinforcement

Day 23 reinforced no more than two major gaps.

### Transaction Failure Responsibility

The reinforced distinctions were:

- A missing rollback call is not a rollback failure.
- A rollback failure requires an attempted rollback that throws an exception.
- Rollback must occur before auto-commit restoration when the operation has failed.
- If a primary failure exists, rollback and restoration failures should be retained as suppressed exceptions.
- If no primary failure exists, a restoration failure may become the main failure.

The learner confirmed understanding after feedback. This same-day confirmation does not justify immediate Mastery promotion.

### Instance Method Invocation

When each object owns its own state, `increment()` should be an instance method called through a specific instance:

`counter.increment()`

The learner confirmed understanding after feedback. Cross-day unprompted evidence remains necessary.

## Mastery Evidence

| Topic | Day 23 evidence |
|---|---|
| Git／CI failure handling | Review supporting evidence |
| JDBC transaction failure responsibility | Weak |
| Static／instance | Weak, close to Review |
| ReDoS | Review candidate |
| Outcome versus invocation | Stable supporting evidence |

No topic was promoted solely because the learner understood an explanation or immediately answered correctly after reinforcement.

## Capability Coverage

The Day 23 review sampled:

- Java／OOP
- Git／CI
- Secure coding
- JDBC／transaction
- Testing evidence

The broader pre-interview coverage still needs representative evidence across:

- Java／OOP
- Dependency injection／design
- Git／CI
- Secure coding
- SQL
- JDBC／transaction
- HTTP／REST
- Testing

Day 24 should sample inheritance, polymorphism, generics, or other insufficiently covered areas while preventing transaction questions from occupying the entire review.

## Presentation and Script

The presentation was built from the canonical Day 21 third-version script.

Canonical baseline:

- Approximately 80% complete
- Produced after three complete readings
- Third complete reading: approximately 04:25
- Correct laboratory name: `Logos Lab`

The five-slide narrative remains:

1. Personal positioning
2. Research capability
3. Career decision and engineering preparation
4. Engineering practice and quality evidence
5. Role fit and closing

Slides provide visual emphasis and evidence. The script retains the complete narrative.

Academic evidence is shown for credibility and is not narrated item by item unless the interviewer asks about it during Q&A.

### Presentation Versions

#### v0.2

The first usable five-slide draft established:

- A white, navy, and teal visual system
- Three concise education entries without years
- Logos Lab research-assistant evidence
- PoRT thesis evidence
- ITAOI 2023 and ICIM 2023 evidence
- Three research-project themes
- Java Task Manager engineering evidence
- Role-fit closing

User review identified:

- An inconsistent thesis-label font size
- Insufficient clarity for smaller non-bold text
- Ambiguous visual emphasis on the Service layer

#### v0.3

Date: 2026-09-09

Modified slides:

- Slide 1: increased supporting-text weight and contrast
- Slide 2: standardized the thesis label and improved evidence readability
- Slide 3: strengthened secondary-text readability
- Slide 4: clarified `Service / transaction boundary`
- Slide 5: increased secondary-text brightness and weight

Reasons:

- Improve readability on a white background
- Make the Service-layer emphasis understandable
- Preserve a consistent Microsoft JhengHei presentation style

Main differences from v0.2:

- More consistent font sizing
- Stronger supporting-text weight
- Higher foreground/background contrast
- Explicit transaction-boundary wording

The canonical narrative and third-version script were not replaced.

## First Timed Rehearsal

Presentation version: `v0.3`

Interval:

- Start: 15:25:08 CST
- Finish: 15:29:40 CST
- Total: `00:04:32`

### Per-slide Timing

| Slide | Duration |
|---|---:|
| Slide 1 | 00:00:38 |
| Slide 2 | 00:00:53 |
| Slide 3 | 00:00:46 |
| Slide 4 | 00:01:18 |
| Slide 5 | 00:00:57 |
| Total | 00:04:32 |

The rehearsal finished within the five-minute target and retained approximately 28 seconds of buffer.

Observed risks:

- The Slide 1 transition sentence was not yet fully familiar.
- Slide 2 evidence could encourage unnecessary academic detail.
- Slide 4 had the highest information density and may be harder to recall under pressure.
- Only one self-rehearsal has been completed.

The presentation remains at `v0.3`.

Future validation should include another cross-day timed rehearsal and, when practical, rehearsal in front of another person.

## Learner Reflection

### Core Outcomes

The learner completed:

- Five controlled cross-topic questions
- Reinforcement of two major gaps
- A complete five-slide presentation draft
- Revision from v0.2 to v0.3
- A complete timed rehearsal in 04:32

### Stable Areas

The learner showed stronger evidence in:

- Git／CI failure handling
- ReDoS identification and mitigation
- Outcome evidence versus invocation evidence

### Main Gaps

The two primary gaps were:

- Transaction failure classification and responsibility
- Correct instance-method invocation

### Presentation Reflection

The five-slide structure can support the complete narrative.

The academic evidence does not need to be narrated item by item.

Slide 2 and Slide 4 require continued observation because their visible evidence may encourage additional explanation or create recall pressure.

### Next-day Handoff

Day 24 should:

- Revalidate transaction failure responsibility with changed wording or scenario
- Revalidate static／instance without prompting
- Sample inheritance, polymorphism, and generics
- Preserve controlled cross-topic coverage
- Avoid allowing transaction questions to dominate the review
- Repeat the five-minute presentation rehearsal
- Observe Slide 2 and Slide 4
- Modify v0.3 only when repeated evidence identifies a concrete need

## Time Summary

Recorded Day 23 interval:

- 2026-09-09 08:54:14–16:32:10 CST

Excluded time:

| Exclusion | Time |
|---|---:|
| Morning rest | 00:08:01 |
| Rest after review | 00:24:07 |
| Lunch | 02:00:18 |
| Afternoon rest | 00:04:24 |
| Pre-Reflection interval | 00:03:39 |
| Total excluded | 02:40:29 |

Effective training time through Reflection:

> 04:57:27

This includes baseline validation, Issue and branch setup, learner-first answers, feedback, corrected explanations, information digestion, stage-gate interaction, presentation production, presentation revision, artifact review, timed rehearsal, and Reflection.

The original 3.5-to-4-hour estimate was exceeded mainly because the complete presentation draft required one revision and two rounds of learner visual validation.

Reasonable answer and information-digestion time was not compressed.

## Workflow Correction

A generated response form was initially presented outside a copyable code block.

A later Reflection stage form also used an incorrect descriptive heading instead of the actual Issue checklist identifier.

The corrected workflow is:

- Present every learner reply form inside a copyable plain-text code block.
- Use the exact Issue checklist identifier in stage-gate forms.
- Treat Reflection as D23-06 rather than creating a separate Reflection Stage Gate.

## Implementation Decision

No concrete, reproducible, and meaningful task-manager implementation or coverage gap was identified.

The Java 21 environment, task-manager source code, dependencies, Maven configuration, CI configuration, and 38-test baseline remain unchanged.

Repository changes are limited to this delivery-stable Day 23 Daily Log.

## Delivery

Final delivery evidence is tracked by GitHub Issue #48, the primary Pull Request, GitHub Actions, and the canonical Day 23 Notion record.
