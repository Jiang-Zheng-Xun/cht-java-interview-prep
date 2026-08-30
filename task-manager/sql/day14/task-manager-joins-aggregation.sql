PRAGMA foreign_keys = ON;

.headers on
.mode column
.nullvalue NULL

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
    (3, 'carol', NULL),
    (4, 'dave', 'Dave Wu');

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


SELECT
    'Dataset verification' AS section;

SELECT
    (SELECT COUNT(*) FROM users) AS user_count,
    (SELECT COUNT(*) FROM tasks) AS task_count,
    (
        SELECT COUNT(*)
        FROM pragma_foreign_key_check
    ) AS foreign_key_violation_count;

SELECT
    id,
    username,
    display_name
FROM users
ORDER BY id;

SELECT
    'Query 1: INNER JOIN users and tasks' AS section;

SELECT
    t.id AS task_id,
    u.username,
    u.display_name,
    t.title,
    t.status,
    t.priority,
    t.due_date
FROM users AS u
INNER JOIN tasks AS t
    ON u.id = t.user_id
ORDER BY
    task_id;

SELECT
    'Query 2: LEFT JOIN includes users without tasks' AS section;

SELECT
    u.id AS user_id,
    u.username,
    t.id AS task_id,
    t.title,
    t.status
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
ORDER BY
    user_id, task_id;

SELECT
    'Query 3: COUNT star versus COUNT task ID' AS section;

SELECT
    u.id AS user_id,
    u.username,
    COUNT(*) AS joined_row_count,
    COUNT(t.id) AS task_count
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
GROUP BY
    u.id,
    u.username
ORDER BY
    u.id;

SELECT
    'Query 4: task priority aggregates by user' AS section;

SELECT
    u.id AS user_id,
    u.username AS username,
    COUNT(t.id) AS task_count,
    SUM(t.priority) AS total_priority,
    ROUND(AVG(t.priority), 2) AS average_priority,
    MIN(t.priority) AS minimum_priority,
    MAX(t.priority) AS maximum_priority
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
GROUP BY
    u.id, u.username
ORDER BY
    u.id;

SELECT
    'Query 5A: WHERE filters rows before grouping' AS section;

SELECT
    u.id AS user_id,
    u.username,
    COUNT(t.id) AS urgent_task_count
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
WHERE t.priority <= 2
GROUP BY
    u.id,
    u.username
ORDER BY
    u.id;

SELECT
    'Query 5B: HAVING filters groups after aggregation' AS section;

SELECT
    u.id AS user_id,
    u.username,
    COUNT(t.id) AS task_count
FROM users AS u
LEFT JOIN tasks AS t
    ON u.id = t.user_id
GROUP BY
    u.id,
    u.username
HAVING COUNT(t.id) >= 3
ORDER BY
    u.id;

SELECT
    'Query 6: tasks above average priority' AS section;

SELECT
    t.id AS task_id,
    t.title,
    t.priority
FROM tasks AS t
WHERE t.priority > (
    SELECT AVG(tasks.priority)
    FROM tasks
)
ORDER BY
    t.priority DESC,
    t.id ASC;
