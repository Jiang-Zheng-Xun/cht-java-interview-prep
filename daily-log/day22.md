# Day 22｜跨主題複習與簡報輕量推進

## Date

2026-09-08

## Goal

Use a lightweight cross-topic retrieval session to review selected Question Bank Weak／Review areas and advance the five-slide first-interview presentation.

Review Java／OOP, Git, dependency injection, secure coding, and JOIN cardinality through learner-first answers and direct feedback.

Inventory one core message, supporting evidence, and remaining gap for each presentation slide while preserving the research-to-engineering narrative.

Retain the existing task-manager and 38-test baseline unless a concrete, reproducible, and meaningful implementation or coverage gap is verified.

## Baseline

Day 22 started from the following verified baseline:

- Branch: `develop`
- Local HEAD: `4d91640`
- Remote `origin/develop`: `4d91640`
- Ahead／behind: `0 / 0`
- Working tree: clean
- Local Day 21 feature branch: absent
- Remote Day 21 feature branch: absent
- Maven tests: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: `BUILD SUCCESS`
- Other anomalies: none

The Day 22 feature branch was created from `4d91640` and synchronized with its same-named remote branch before training continued.

## Cross-topic Retrieval Review

Day 22 reviewed five questions, one at a time:

1. Java／OOP: static method versus instance field
2. Git: working tree, staging area, and local repository
3. Dependency injection: constructor injection and interface dependency
4. Secure coding: ReDoS and catastrophic backtracking
5. SQL: JOIN cardinality and row multiplication

Each answer was assessed through:

- Concept accuracy
- Wording
- Scenario application
- English-reading comprehension

Corrected answers were preserved after feedback without requiring an immediate rewrite.

### Answer Timing

| Question | Topic | Answer time |
|---|---|---:|
| Q1 | Static／instance | 00:01:38 |
| Q2 | Git three areas | 00:02:49 |
| Q3 | Dependency injection | 00:16:34 |
| Q4 | ReDoS | 00:06:22 |
| Q5 | JOIN row multiplication | 00:15:27 |
| Total | — | 00:42:50 |

The overall review block ran from 17:09:38 to 18:50:38 CST and took `01:41:00`.

The difference between pure answer time and overall block time includes reading, feedback, corrected answers, information digestion, and transitions. These activities are part of effective training time.

## Static Method and Instance Field

An instance field belongs to a specific object.

A static method belongs to the class and has no specific object instance or `this`. It therefore cannot directly determine which object's instance field should be accessed.

Attempting to access the instance field directly from the static method produces a compile-time error.

When every object should maintain its own state, the behavior should normally be an instance method:

> A static method has no specific instance or `this`, so it cannot directly access an instance field. If every object maintains its own count, `increment()` should be an instance method called through a specific `Counter` object.

The learner recognized the static／instance conflict but did not initially state the lack of a specific instance or `this`, and did not clearly identify conversion to an instance method as the design correction.

Mastery evidence remains Weak.

## Git Working Tree, Staging Area, and Local Repository

The working tree contains the files currently being edited.

`git add` records the selected content as a snapshot in the staging area. It does not move the original file away from the working tree and does not upload anything to GitHub.

`git commit` saves the staged snapshot as a new commit in the local repository.

If no further edits occur after the commit:

- The working-tree version matches the latest commit.
- The staging area has no staged difference from the latest commit.
- The local repository contains the new commit.
- The remote repository does not receive the commit until `git push`.

The learner understood the overall `add` → `commit` → `push` flow but initially described `git add` as pushing the file from the working tree to the staging area.

Mastery evidence remains Review.

## Constructor Injection

When a Controller directly creates its own concrete Service, it is coupled to both:

- The concrete Service type
- The way the Service object is constructed

Constructor injection moves dependency creation outside the dependent class:

> Define a contract, create an implementation externally, pass it through the constructor, and let the Controller depend only on the contract.

The corrected responsibility chain is:

```text
External creation
→ Constructor parameter
→ Interface dependency
→ Replaceable implementation or test double
```

Depending on an interface allows the application to inject a real implementation in production and a fake or mock in tests.

The learner initially understood that an interface could reduce coupling and allow implementation replacement, but did not fully describe external dependency creation, constructor transfer, or the testing benefit.

Mastery evidence is a new Weak candidate. Understanding the corrected explanation is not sufficient for promotion; future cross-day, unprompted, changed-scenario retrieval is required.

## ReDoS and Catastrophic Backtracking

A pattern such as `(a+)+$` contains overlapping nested quantifiers.

A long input containing many `a` characters followed by a nonmatching character can cause a backtracking regex engine to explore a very large number of possible groupings before rejecting the input.

The primary security impact is resource exhaustion:

- Excessive CPU usage
- Long response time
- Occupied request-processing threads
- Reduced service availability
- Possible denial of service

Practical controls include:

- Rewriting ambiguous nested quantifiers
- Limiting input length
- Using a matching timeout where available
- Using a non-backtracking engine where appropriate
- Applying request-rate controls

The attacker in the reviewed scenario controlled the input string, not the regular expression.

The learner recognized that nested regex structures could consume substantial resources but initially mixed together attacker-controlled input, the server-side regex, and incorrect result output.

Mastery evidence remains Weak.

## JOIN Cardinality and Row Multiplication

A JOIN returns one output row for every pair of rows that satisfies the join condition.

If one customer has three different matching orders, three output rows are valid one-to-many output. Repetition of the parent columns does not by itself mean that duplicate data exists.

A repeated foreign key is also not automatically a duplicate. In a one-to-many design, multiple child rows are expected to reference the same parent key.

The diagnostic order is:

1. Confirm the expected relationship cardinality.
2. Check uniqueness of keys that are required to be unique.
3. Determine whether repeated foreign keys are valid for the relationship.
4. Verify that the child rows have distinct identities.
5. Verify that the join condition uses the correct and complete columns.
6. Modify the query or data only after the row multiplication is explained.

`DISTINCT` should not be used first because it can hide a wrong join condition, missing join key, actual duplicate data, or valid one-to-many output.

The learner identified cardinality, join conditions, and key uniqueness as diagnostic directions, but again interpreted valid one-to-many output as likely duplicate rows.

Because this confusion persisted across days and a changed scenario, Mastery remains Weak.

## Reinforcement

Day 22 reinforced no more than two major gaps.

### Dependency Injection

The reinforced model was:

```text
Define the contract
→ Create the implementation externally
→ Pass the dependency through the constructor
→ Let the Controller depend on the contract
```

The learner confirmed understanding that constructor injection transfers dependency creation outside the Controller and allows implementation or test-double replacement.

### JOIN Cardinality

The reinforced model was:

```text
One output row per matching pair
→ Repeated parent values may be valid
→ Repeated foreign key does not equal duplicate child row
→ Verify cardinality, identities, keys, and join conditions
```

The learner confirmed understanding of the difference between valid one-to-many output and actual duplicate rows.

These confirmations occurred after explanation and therefore do not independently justify promotion to Review or Stable.

## Mastery Evidence

| Topic | Day 22 evidence |
|---|---|
| Static／instance | Weak |
| Git three areas | Review |
| Dependency injection | Weak candidate |
| ReDoS | Weak |
| JOIN row multiplication | Weak |

No topic was promoted to Stable merely because the corrected answer was understood.

Future promotion requires reliable evidence across days, without prompting, and in changed scenarios.

## Five-slide Presentation Inventory

The intended audience is the Chunghwa Telecom first-interview panel.

The communication goal is to show that research-trained analysis has been actively converted into verifiable engineering preparation and can contribute to large-scale information-system development and maintenance.

The narrative sequence is:

```text
Personal positioning
→ Research capability
→ Career decision and action
→ Engineering evidence
→ Chunghwa Telecom contribution
```

### Slide 1 — Personal Positioning

Core message:

> 研究訓練讓我具備系統化分析問題的能力，而我正主動把它轉化為工程實務能力。

Supporting evidence:

- 江政勳
- Applying for Chunghwa Telecom Information Systems Planning and Development
- National Tsing Hua University doctoral research experience
- Passed the doctoral qualification examination
- Currently planning a temporary study leave to enter industry

Remaining work:

- Confirm the official presentation requirements.
- Keep the title slide minimal.
- Do not include a portrait unless the official instructions require or support it.
- Consider a small GitHub link or QR code as portfolio evidence rather than an identity label.

### Slide 2 — Research Capability

Core message:

> 區塊鏈研究訓練了我拆解複雜系統、驗證假設與清楚溝通技術的能力。

Supporting evidence:

- Blockchain consensus, data structures, performance, and security research
- Participation in paper publication and research-project writing
- Research-assistant experience
- Teaching-assistant experience in data structures and blockchain-related courses
- Proof-of-Refundable-Tax research

Research wording should remain evidence-calibrated.

PoRT does not yet have decisive data proving comprehensive improvement. Its interview value is that related paper publication and project participation demonstrate research analysis, problem modeling, mechanism design, and validation experience.

The presentation should use terms such as proposed, designed, and explored rather than claiming a conclusively proven improvement.

Remaining work:

- Select one research example understandable to a non-blockchain audience.
- Simplify the tradeoff among performance, security, decentralization, and sustainable incentives.
- Avoid overloading the slide with Bitcoin, Ethereum, PoS, and full PoRT protocol details.

### Slide 3 — Career Decision and Concrete Action

Core message:

> 通過資格考與指導教授退休成為重新確認方向的契機，我選擇用具體行動轉向工程職涯。

Supporting evidence:

- Passed the doctoral qualification examination
- No major remaining course pressure
- Desire to gain experience in team development, system delivery, and long-term maintenance
- Continued Java, SQL, Maven, GitHub, CI/CD, testing, and AI-assisted development practice

The wording must not imply that the doctoral degree has already been obtained.

Remaining work:

- Keep the explanation positive and concise.
- Show that the transition is supported by concrete preparation rather than presented as an escape from research difficulty.
- Align terminology with official job and presentation instructions when received.

### Slide 4 — Engineering Evidence

Core message:

> 我以可驗證的 Java 專案與品質流程，證明自己正在補足工程實務能力。

Supporting evidence:

```text
HTTP request
→ Controller validation
→ Service transaction
→ Repository／SQLite
→ Integration tests／CI
```

Additional evidence:

- Controller／Service／Repository responsibility boundaries
- HTTP and JSON processing
- SQLite persistence
- Transaction and rollback behavior
- 38 passing tests
- Issue, feature branch, Pull Request, and GitHub Actions workflow
- AI-assisted output validated through tests, CI, system evidence, and human judgment

The slide should demonstrate completeness through one end-to-end responsibility and validation chain rather than presenting a dense inventory of tools.

Remaining work:

- Select one main process visual.
- Keep the slide readable within the five-minute limit.
- Retain AI security and verification boundaries primarily as spoken explanation.

### Slide 5 — Chunghwa Telecom Contribution

Core message:

> 我希望把研究分析能力與工程執行力，投入中華電信大型資訊系統的開發與長期維運。

Supporting evidence:

- Interest in large-scale systems and public services
- Interest in cross-team collaboration, reliability, and maintenance
- Recent engineering preparation and verifiable delivery evidence

Closing contribution statement:

> 我希望把研究訓練形成的問題分析能力，結合近期累積的工程實作與品質驗證經驗，投入中華電信大型資訊系統的開發、協作與長期維運。

Remaining work:

- Align the final wording with the official role keywords.
- Confirm the official template, page count, topic, and submission rules.
- Produce the first complete framework draft, then validate it with the complete script and timed readings.

## Presentation Decision

The five-slide structure is accepted as the initial framework.

The immediate priority is to produce a complete first draft and then iteratively refine the visible content and script until it is suitable for the first interview.

The current target remains a presentable version by approximately 2026-09-12, subject to official instructions.

## Learner Reflection

### Core outcomes

The learner identified completion of the five-slide presentation’s core messages, evidence, and remaining-gap inventory as the primary Day 22 outcome.

### Most Representative Weaknesses

The learner identified:

- Static／instance: did not immediately state that a static method has no specific instance or `this`
- Dependency injection: understood coupling reduction but had not stabilized the complete constructor-injection responsibility chain
- ReDoS: mixed attacker-controlled input, the dangerous regex, and availability impact

### Most Important Technical Understanding

Constructor injection:

```text
External creation
→ Constructor injection
→ Interface dependency
→ Replaceable implementation or test double
```

JOIN:

```text
One row per matching pair
→ Repeated foreign key does not equal duplicate child rows
```

### Timing and Feedback Correction

Future retrieval-practice questions should reserve at least three to four minutes for learner answering.

Three to four minutes is a minimum planning allowance rather than a mandatory maximum.

Outside a mock written test or mock interview, each learner answer is followed by analysis and a corrected version. Reading, understanding, information digestion, follow-up interaction, and transitions make the overall block substantially longer than pure answer time, which is normal.

The answer strategy remains:

1. State the conclusion and core concept first.
2. Add details, reasons, process, limitations, and evidence when time remains.
3. Prioritize stable scoring keywords and concepts over reproducing a long model answer.

During a mock written test or mock interview, feedback is withheld until the attempt is complete.

### Presentation Priority

The next presentation priority is to produce a complete framework draft and then progressively refine it with the full script until it becomes suitable for the first interview.

### Strength

The learner retained partial understanding across all five sampled topics and could provide an initial answer rather than being unable to respond.

### Risk

The learner is uncertain whether approximately ten remaining days are sufficient to:

- Move a meaningful portion of the Question Bank toward Review or Stable
- Complete multiple mock written tests
- Complete multiple mock interviews
- Finish and refine the presentation
- Build confidence from measurable improvement

This risk must be evaluated using realistic daily review capacity, cross-day retrieval requirements, presentation workload, and the number of mock sessions that can fit before the interview.

### Next-day Handoff

Before finalizing the Day 23 workload, evaluate and present a proposal based on:

- The fixed daily progression established after Day 21
- The learner’s concern about the remaining ten-day window
- Current Weak／Review／Stable distribution
- Realistic per-question answering, feedback, and digestion time
- Cross-day and changed-scenario evidence required for Mastery promotion
- Presentation first-draft and refinement deadlines
- The number and spacing of mock written tests and mock interviews
- Workload and confidence risks

The learner will review and explicitly approve or revise the proposed schedule before Day 23 training begins.

## Time Summary

Recorded working intervals through Reflection:

- 2026-09-08 16:44:56–19:04:38 CST
- 2026-09-08 20:09:20–21:25:00 CST

Excluded time:

| Exclusion | Time |
|---|---:|
| Dinner | 01:04:42 |

Effective training time through Reflection:

> 03:35:22

This calculation includes baseline validation, Issue and branch setup, reading, thinking, answering, feedback, corrected answers, information digestion, stage-gate interaction, workflow correction, presentation planning, and Reflection.

It excludes dinner.

The original approximately 120-minute plan was exceeded because the five-question review included full learner-first answers, individual feedback, corrected versions, information digestion, timing-rule correction, and presentation-content review.

## Workflow Corrections

Day 22 initially treated the Question Bank’s 30-to-60-second concise interview answer as if it were the learner’s complete answer-time limit.

This was incorrect.

The corrected interpretation separates:

- Concise saved answer length
- Written-test answer allocation
- Oral interview interaction time
- General retrieval-practice answer time
- Feedback and information-digestion time

For ordinary retrieval practice:

- Reserve at least three to four minutes for the learner’s answer.
- Do not treat that allowance as a mandatory upper limit.
- Include feedback, corrected answers, reading, understanding, digestion, and interaction in effective training time.

For mock written tests and mock interviews:

- Do not provide analysis between questions.
- Provide scoring and feedback only after the complete attempt.
- Continue using the minimum-complete-answer strategy: conclusion and core concepts first, then expand when time remains.

The earlier time-based criticism of Q3 and Q5 was withdrawn. Their Weak evidence is based on substantive concept gaps, not on exceeding an incorrect 30-to-60-second limit.

This is a day-local correction that restores consistency with the existing interview-answer and effective-time rules. It does not silently modify the Command Registry.

## Implementation Decision

No concrete, reproducible, and meaningful task-manager implementation or coverage gap was identified.

The Day 22 weaknesses were knowledge-retrieval and explanation weaknesses rather than production-code defects.

The task-manager remains unchanged, and the 38-test baseline is retained.

## Delivery

Final delivery evidence is tracked by GitHub Issue #46, the primary Pull Request, GitHub Actions, and the canonical Day 22 Notion record.
