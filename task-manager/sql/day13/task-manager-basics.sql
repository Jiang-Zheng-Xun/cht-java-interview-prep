.bail on
.headers on
.mode column

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    username TEXT NOT NULL UNIQUE
        CHECK (length(trim(username)) > 0),
    display_name TEXT
);

CREATE TABLE tasks (
    id INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title TEXT NOT NULL
        CHECK (length(trim(title)) > 0),
    status TEXT NOT NULL
        DEFAULT 'PENDING'
        CHECK (
            status IN (
                'PENDING',
                'IN_PROGRESS',
                'COMPLETED'
            )
        ),
    priority INTEGER NOT NULL
        DEFAULT 3
        CHECK (priority BETWEEN 1 AND 5),
    due_date TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (id, username, display_name)
VALUES
    (1, 'alice', 'Alice Chen'),
    (2, 'bob', 'Bob Lin'),
    (3, 'carol', NULL);

INSERT INTO tasks (
    id,
    user_id,
    title,
    status,
    priority,
    due_date
)
VALUES
    (1, 1, 'Review SQL basics', 'PENDING', 2, '2026-08-30'),
    (2, 1, 'Complete HackerRank', 'COMPLETED', 3, '2026-08-29'),
    (3, 2, 'Write daily log', 'IN_PROGRESS', 1, NULL),
    (4, 2, 'Practice Java', 'PENDING', 4, '2026-09-01'),
    (5, 3, 'Review NULL behavior', 'PENDING', 2, NULL),
    (6, 1, 'Prepare SQL interview', 'PENDING', 1, '2026-08-29'),
    (7, 3, 'Read database notes', 'PENDING', 5, '2026-09-03');

-- status is omitted so that DEFAULT 'PENDING' is applied.
INSERT INTO tasks (
    id,
    user_id,
    title,
    priority,
    due_date
)
VALUES
    (8, 2, 'Check constraints', 3, '2026-08-31');

SELECT 'Query 1: all tasks ordered by ID' AS section;

SELECT
    id,
    user_id,
    title,
    status,
    priority,
    due_date
FROM tasks
ORDER BY id;

SELECT 'Query 2: pending tasks ordered by due date' AS section;

SELECT
    id,
    title,
    priority,
    due_date
FROM tasks
WHERE status = 'PENDING'
ORDER BY
    due_date IS NULL,
    due_date,
    id;

SELECT 'Query 3: tasks belonging to user 1' AS section;

SELECT
    id,
    title,
    status
FROM tasks
WHERE user_id = 1
ORDER BY id;

SELECT 'Query 4: high-priority tasks using IN' AS section;

SELECT
    id,
    title,
    priority
FROM tasks
WHERE priority IN (1, 2)
ORDER BY priority, id;

SELECT 'Query 5: medium-priority range using BETWEEN' AS section;

SELECT
    id,
    title,
    priority
FROM tasks
WHERE priority BETWEEN 2 AND 4
ORDER BY priority, id;

SELECT 'Query 6: titles containing SQL using LIKE' AS section;

SELECT
    id,
    title
FROM tasks
WHERE title LIKE '%SQL%'
ORDER BY id;

SELECT 'Query 7: tasks without a due date' AS section;

SELECT
    id,
    title,
    due_date
FROM tasks
WHERE due_date IS NULL
ORDER BY id;

SELECT 'Query 8: pending urgent or undated tasks' AS section;

SELECT
    id,
    title,
    status,
    priority,
    due_date
FROM tasks
WHERE status = 'PENDING'
  AND (
      priority IN (1, 2)
      OR due_date IS NULL
  )
ORDER BY id;