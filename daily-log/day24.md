# Day 24｜OOP 抽檢、跨日驗證與簡報試講

## Date

2026-09-10

## Goal

Validate insufficiently covered OOP concepts and the two Day 23 cross-day gaps through learner-first controlled retrieval.

Complete another five-minute rehearsal using presentation v0.3, evaluate the timing and fluency risks on Slides 2 and 4, and preserve the current presentation unless concrete evidence supports a minimal versioned change.

Keep the task-manager at its existing 38-test baseline unless a concrete, reproducible, and meaningful implementation or coverage gap is verified.

## Baseline

Day 24 started from the following verified baseline:

- Repository root: `/home/logos/projects/cht-java-interview-prep`
- Branch: `develop`
- Local develop HEAD: `186e719`
- Remote `origin/develop`: `186e719`
- Ahead／behind: `0 / 0`
- Working tree: clean
- Local branches: `develop`, `main`
- Remote branches: `origin/develop`, `origin/main`
- Day 23 local branch: absent
- Day 23 remote branch: absent
- Maven tests: 38
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: `BUILD SUCCESS`

No concrete, reproducible, and meaningful implementation or coverage gap was found.

The task-manager remained unchanged.

The Day 24 branch was created from `186e719`:

`feature/day24-oop-cross-day-presentation-rehearsal`

The branch tracks its same-named remote branch.

## Controlled Review

Day 24 completed five learner-first controlled questions:

1. Inheritance versus composition
2. Runtime polymorphism
3. Generic class versus generic method
4. Static method and instance-state ownership
5. Transaction failure responsibility

The questions provided representative coverage of inheritance, polymorphism, and generics while reserving only one transaction slot.

Static／instance and transaction used changed wording or scenarios to produce cross-day evidence.

### Answer Timing

| Question | Topic | Answer time |
|---|---|---:|
| Q1 | Inheritance versus composition | 00:03:07 |
| Q2 | Runtime polymorphism | 00:04:02 |
| Q3 | Generic class versus generic method | 00:07:07 |
| Q4 | Static method and instance state | 00:05:51 |
| Q5 | Transaction failure responsibility | 00:05:45 |
| Total | — | 00:25:52 |

The overall review block included reading, answering, feedback, corrected answers, information digestion, and transitions.

## Review Evidence

### Q1 — Inheritance versus Composition

The scenario described a `NotificationService` that extended `EmailSender` only to reuse validation logic, while later needing to support email, SMS, and push notifications without changing its main workflow.

The learner recognized that `extends` represents inheritance but reversed the `is-a` relationship and selected inheritance instead of composition.

For:

```java
class NotificationService extends EmailSender
```

the semantic relationship is:

`NotificationService is an EmailSender`

It does not mean:

`EmailSender is a NotificationService`

The scenario does not establish a reasonable `is-a` relationship. A notification service uses a notification-sending capability but is not inherently an email sender.

A better design makes `NotificationService` depend on a `NotificationSender` interface and composes it with `EmailSender`, `SmsSender`, or `PushSender`. The workflow can then use the abstraction while replacing the concrete implementation.

Evidence:

- Concept: understood that `extends` creates inheritance, but selected the wrong relationship
- Wording: mentioned `is-a`, but reversed its direction
- Scenario application: did not recognize inheritance used only for method reuse as a coupling problem
- English reading: understood the required notification types but did not fully use the phrases `only to reuse` and `without changing its main workflow`

Mastery evidence: Weak.

The previous Review evidence was insufficient under a changed scenario.

### Q2 — Runtime Polymorphism

The learner correctly determined that:

```java
PaymentMethod method = new MobilePayment();
method.pay(500);
```

executes `MobilePayment.pay()`.

The reference type `PaymentMethod` determines which operations are available at compile time. The actual object type `MobilePayment` determines which overridden instance method runs at runtime.

The learner initially connected the result to the constructor call. The constructor creates the object, but dynamic method dispatch selects the overridden method.

Corrected explanation:

> `MobilePayment.pay()` runs. The reference type is `PaymentMethod`, but the variable points to a `MobilePayment` object. Java verifies the `pay()` operation through the reference type at compile time and uses dynamic method dispatch to select `MobilePayment.pay()` from the actual object at runtime.

Evidence:

- Concept: correctly selected the actual implementation
- Wording: needed to distinguish object construction from method dispatch
- Scenario application: correctly used the actual object type
- English reading: correctly understood implementation selection at runtime

Mastery evidence: Review supporting evidence.

This was cross-day, unprompted, code-based evidence. Runtime polymorphism can move from Weak to Review, but it does not yet have sufficient evidence for Stable.

### Q3 — Generic Class versus Generic Method

The learner understood that the storage logic should support `String`, `Integer`, and `Task`, but proposed `Object<T>`, which is not valid because Java's `Object` class does not declare a type parameter.

The learner also used invariance as the main explanation instead of deciding whether the type parameter belongs to the object's state.

When a type parameter determines an instance field and remains consistent throughout an object's lifetime, the class should be generic:

```java
class Storage<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
```

When a type parameter belongs only to one operation and the class does not retain that state, the method can be generic:

```java
public static <T> T first(List<T> values) {
    return values.get(0);
}
```

Evidence:

- Concept: recognized the need for a reusable type-safe design but did not clearly distinguish class-level and method-level type scope
- Wording: `Object<T>` was an invalid Java type expression
- Scenario application: identified the different target types but did not use object state as the primary decision rule
- English reading: understood that the storage logic must support multiple types but did not fully apply `stored type belongs to the object's state`

Mastery evidence: Weak.

The changed scenario still exposed a core distinction that requires future cross-day validation.

### Q4 — Static Method and Instance-State Ownership

The learner correctly determined that a static method cannot directly access an instance field and correctly proposed changing `markCompleted()` to an instance method.

The remaining error was creating a new `DownloadTracker` inside `markCompleted()` and modifying that new object's field.

The correct design is:

```java
class DownloadTracker {
    private int completed;

    public void markCompleted() {
        this.completed++;
    }
}
```

The caller creates the object and calls:

```java
DownloadTracker tracker = new DownloadTracker();
tracker.markCompleted();
```

The object referenced by `tracker` is the receiver. Inside the instance method, `this` refers to that same receiver.

Creating another tracker inside the method modifies a different object and does not modify the original receiver.

Evidence:

- Concept: correctly understood that a static context has no `this`
- Wording: the static and instance distinction was mostly clear
- Scenario application: still confused the receiver object with a new object created inside the method
- English reading: correctly understood that each object must maintain its own count

Mastery evidence: Weak.

This cross-day, unprompted, changed-scenario check showed progress in selecting an instance method, but receiver ownership and object-qualified invocation remain unstable.

### Q5 — Transaction Failure Responsibility

The learner correctly classified the failure sequence:

1. The second `INSERT` throws `SQLException A`.
2. `SQLException A` is the primary failure.
3. The system attempts rollback.
4. `rollback()` throws `SQLException B`.
5. `SQLException B` is the rollback failure.
6. The rollback failure is preserved using `A.addSuppressed(B)`.
7. The system rethrows `SQLException A`.

`SQLException B` must not replace A because A is the original failure that stopped the business operation and triggered rollback. Replacing A with B would lose the original causal information.

Evidence:

- Concept: correctly distinguished primary and rollback failures
- Wording: described the causal order clearly
- Scenario application: correctly handled a rollback call that actually throws
- English reading: correctly understood primary failure, rollback failure, suppressed exception, and replacement

Mastery evidence: Review.

This cross-day, unprompted, changed-scenario result corrected the Day 23 confusion between a missing rollback call and an actual rollback failure. Transaction can move from Weak to Review, but it does not yet have sufficient evidence for Stable.

## Reinforcement

Day 24 reinforced no more than two major gaps.

### Generic Type Scope

The decision rule is:

- Use a generic class when the type parameter belongs to the object's state.
- Use a generic method when the type parameter belongs only to one method invocation.

A generic class declares its type parameter with the class:

```java
class Storage<T>
```

A generic method declares its own type parameter before the return type:

```java
public static <T> T first(...)
```

`Object<T>` is invalid because `Object` is not a generic class.

The learner understood the corrected distinction after feedback. This same-day understanding does not justify a Mastery promotion.

### Receiver Object and Instance Ownership

The responsibility chain is:

- The caller holds an object reference.
- The caller invokes an instance method through that reference.
- The referenced object becomes the receiver.
- Inside the method, `this` refers to that receiver.
- `this.completed++` modifies the receiver's own state.

Creating another object inside the instance method modifies a different object.

The learner understood the corrected relationship after feedback. This same-day understanding does not justify a Mastery promotion.

Inheritance versus composition remained Weak but was not selected for additional same-day reinforcement because Day 24 preserved the two-gap limit.

## Mastery Evidence

| Topic | Day 24 evidence | Result |
|---|---|---|
| Inheritance versus composition | Changed-scenario failure | Weak |
| Runtime polymorphism | Cross-day, unprompted, code scenario | Review |
| Generic class versus generic method | Changed-scenario failure | Weak |
| Static versus instance ownership | Partial progress; receiver confusion remains | Weak |
| Transaction failure responsibility | Cross-day, unprompted, changed scenario | Review |

No topic was promoted solely because the learner understood feedback or answered correctly during same-day reinforcement.

## Capability Coverage

Day 24 sampled:

- Java／OOP inheritance
- Runtime polymorphism
- Generics
- Static／instance state
- JDBC／transaction failure responsibility
- English reading within technical scenarios

The broader pre-interview coverage remains:

- Java／OOP
- Dependency injection／design
- Git／CI
- Secure coding
- SQL
- JDBC／transaction
- HTTP／REST
- Testing

Future controlled review should prioritize current Weak areas without allowing them to occupy the complete daily question set.

## Presentation Baseline

Presentation:

`cht-interview-day23-presentation-v0.3.pptx`

Script:

Canonical third-version five-page script

The presentation and script retain the same narrative:

1. Personal positioning
2. Research capability
3. Career decision and engineering preparation
4. Engineering practice and quality evidence
5. Role fit and closing

The correct laboratory name remains:

`Logos Lab`

Academic achievements remain visual evidence. They are not narrated item by item unless the interviewer asks for more detail during Q&A.

## Second Timed Rehearsal

Presentation version: `v0.3`

Interval:

- Start: 11:07:43 CST
- Finish: 11:13:13 CST
- Total: `00:05:30`
- Interrupted or restarted: no

### Per-slide Timing

| Slide | Day 23 | Day 24 | Difference |
|---|---:|---:|---:|
| Slide 1 | 00:00:38 | 00:00:34 | −00:00:04 |
| Slide 2 | 00:00:53 | 00:01:29 | +00:00:36 |
| Slide 3 | 00:00:46 | 00:01:08 | +00:00:22 |
| Slide 4 | 00:01:18 | 00:01:36 | +00:00:18 |
| Slide 5 | 00:00:57 | 00:00:43 | −00:00:14 |
| Total | 00:04:32 | 00:05:30 | +00:00:58 |

The rehearsal exceeded the five-minute target by 30 seconds.

Most of the increase occurred on Slides 2, 3, and 4.

### Slide 2 Evidence

The learner expanded briefly on PoRT's long-term incentive and maintenance considerations.

The learner was less fluent when moving from blockchain design tradeoffs to the research method of clarifying constraints, comparing tradeoffs, and validating ideas with a prototype.

The learner also lacked confidence when describing research projects and achievements.

The recommended spoken structure is:

1. Begin with the research experience.
2. State the performance, security, and decentralization tradeoff.
3. Explain the method: clarify constraints, compare designs, and validate through a prototype.
4. Treat the displayed achievements as supporting evidence rather than narrating each item.

Detailed PoRT incentive design remains available for Q&A rather than the five-minute introduction.

### Slide 4 Evidence

Slide 4 remained the most information-dense page, and the learner's spoken organization was not yet fluent.

The essential spoken structure is:

1. The Java Task Manager strengthened engineering practice.
2. The project uses Controller, Service, and Repository layers.
3. The Service owns the transaction boundary; a failed operation rolls back to prevent partial updates.
4. Tests, CI, and human judgment validate the result, including AI-assisted work.

The five-minute version does not need to explain every Controller, Repository, SQLite, safe-error, test-count, or AI-assistance detail.

These details remain available for Q&A.

### Transition Evidence

The transition from Slide 1 to Slide 2 was not yet natural.

A possible spoken cue is:

> 先以我的研究經驗為例。

This remains a rehearsal cue. It did not trigger a script revision.

### Presentation Decision

The presentation remains at `v0.3`.

The canonical v0.3 uses the following formatting for all three research-evidence labels:

- ITAOI 2023: 12.75 pt, bold
- ICIM 2023: 12.75 pt, bold
- Logos Lab: 12.75 pt, bold

The learner's local copy initially displayed ICIM 2023 and Logos Lab at 12 pt. Those two local labels were manually changed to 12.75 pt to synchronize the local copy with the canonical v0.3.

This was local synchronization rather than a new presentation revision:

- New version created: no
- Canonical presentation changed: no
- Canonical script changed: no
- Modified narrative: no
- Modified slides relative to canonical v0.3: none

Future rehearsal should compare performance with and without the script and should use concise spoken anchors for Slides 2–4 before considering a presentation or script revision.

## Learner Reflection

The learner identified the following core outcomes:

- Transaction failure responsibility became more reliable.
- Runtime polymorphism could be explained using the actual object and selected implementation.
- The complete five-minute rehearsal continued despite hesitation and produced realistic evidence.
- The specific speaking risks on Slides 2 and 4 became visible.

The learner identified the following technical understanding:

- `A extends B` represents the relationship `A is a B`.
- A generic class type parameter affects the object, while a generic method type parameter belongs to one method invocation.
- A static method belongs to a class-level context. It can directly access static members but cannot directly access an instance field without an object reference.
- The caller holds an object reference and invokes an instance method through it. The receiver is the called object, and `this` refers to that receiver.

The learner identified inheritance／composition and generic type scope as the clearest current weaknesses.

A major cause was reading the scenario too quickly and answering before identifying the actual design question.

Future validation should use changed wording or scenarios without compressing reasonable answer time.

The 05:30 rehearsal exceeded the target mainly because the script was not yet familiar and the learner lacked confidence when speaking without it.

Slides 2 and 4 should first be practiced using a stable sequence of essential points. Further rehearsal should determine whether the remaining difficulty comes from the script itself or from familiarity before any broad rewrite occurs.

The learner considered the Day 24 workload and pacing appropriate.

## Time Record

### Effective Work

| Work item | Interval or duration | Effective time |
|---|---|---:|
| D24-01 baseline | 09:07:28–09:16:38 | 00:09:10 |
| Branch setup | 09:23:16–09:27:06 | 00:03:50 |
| D24-02 controlled review | 09:27:31–10:21:06, excluding the recorded break | 00:45:30 |
| D24-03 reinforcement | 10:25:22–10:59:42 | 00:34:20 |
| D24-04 rehearsal, analysis, and stage gate | 11:05:54–12:06:28 | 01:00:34 |
| D24-05 version decision and correction | 14:24:25–14:30:38 | 00:06:13 |
| D24-06 Reflection and artifact verification | 14:30:38–15:20:40 | 00:50:02 |
| Effective time before Daily Log and delivery | — | 03:29:39 |

### Excluded Time

| Type | Interval | Duration |
|---|---|---:|
| Short break | 09:28:18–09:36:23 | 00:08:05 |
| Lunch | 12:06:28–14:24:25 | 02:17:57 |

## Artifact Verification

Before creating the Daily Log, the following state was verified:

- Branch: `feature/day24-oop-cross-day-presentation-rehearsal`
- HEAD: `186e719`
- Working tree: clean
- `git diff --check`: no output
- `git diff --stat`: no output
- task-manager diff: none
- Existing Day 24 Daily Log: absent
- Presentation baseline: v0.3
- Canonical third-version script: unchanged
- Additional artifact or implementation gap: none

## Day 25 Handoff

Day 25 should continue controlled coverage without allowing repeated Weak topics to occupy the full question set.

Priority areas:

- Inheritance versus composition: revalidate the direction and meaning of `is-a` in a changed design scenario.
- Generics: revalidate whether the type belongs to object state or one method invocation.
- Static／instance: revalidate caller, receiver, `this`, and state ownership.
- Preserve representative coverage of the remaining Java／OOP, DI／design, Git／CI, secure coding, SQL, JDBC／transaction, HTTP／REST, and testing units.
- Use full simulation results to verify whether daily controlled sampling accurately represents performance.
- Compare a presentation rehearsal with the script and a rehearsal using only slide anchors.
- Continue from presentation v0.3 and the canonical third-version script.
- Modify the presentation or script only when repeated evidence supports a minimal versioned change.
- Keep official interview information higher priority than provisional timing assumptions.

The default remains five learner-first controlled questions, with a maximum of six and no more than two reinforced major gaps.

Reasonable answering, feedback, and digestion time must not be compressed merely to increase question count.

Final delivery evidence is tracked by the linked GitHub Issue, Pull Request, GitHub Actions, and the canonical Day 24 Notion record.
