# Day 15 — SQLite JDBC and CI

## Objective

Integrate the Java task manager with SQLite through JDBC, use parameterized SQL, verify database constraints with repeatable integration tests, and run the Maven test suite in GitHub Actions.

## Implementation

- Added the Xerial SQLite JDBC dependency to the Maven project.
- Added `SQLiteDatabase` to validate SQLite JDBC URLs, open connections, enable foreign-key enforcement, and initialize the task schema.
- Added `SQLiteTaskRepository` for inserting and loading tasks.
- Used `PreparedStatement` parameter binding so values remain separate from the SQL template.
- Traversed `ResultSet` with `next()` and mapped rows back to `Task` objects.
- Added schema constraints for nonblank titles and valid completion values.
- Applied resource ownership: callers close `Connection`; repository/helper methods close the statements and result sets they create.
- Added demonstration programs for connection setup, parameter binding, result mapping, and constraint behavior.

## Testing

- Added `SQLiteTaskRepositoryIntegrationTest` using a fresh in-memory SQLite database for each test.
- Verified insertion order and row-to-object mapping.
- Verified injection-shaped input is stored as data.
- Verified blank titles and invalid completion values are rejected by database constraints.
- Verified foreign-key enforcement is enabled.
- Ran the complete Maven suite successfully: 22 tests, 0 failures, 0 errors, 0 skipped.

## Continuous Integration

- Added `.github/workflows/java-ci.yml`.
- The workflow runs for pushes to `develop` and pull requests whose base branch is `develop`.
- The runner uses Ubuntu and Java 21.
- Maven runs from `task-manager` with:
  `mvn --batch-mode clean test`
- Pull request #30 completed the CI workflow successfully.

## Technical Review

Key distinctions reinforced today:

- `.gitignore` controls which local files Git tracks; workflow `paths` and `paths-ignore` control CI triggers.
- Java validation rejects obvious invalid input early, parameter binding separates values from SQL syntax, and database constraints provide the final integrity boundary.
- An integration test is classified by the real components exercised together: repository code, JDBC driver, SQLite engine, SQL, schema constraints, and row mapping. Maven and GitHub Actions execute the test but are not part of that integration boundary.
- `ResultSet.next()` advances the cursor to the next row; the first call moves it from before the first row to the first row.

## Delivery

- Issue: #29
- Pull request: #30
- CI result: success
