#!/usr/bin/env bash

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TASK_MANAGER_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
SRC_DIR="$TASK_MANAGER_DIR/src/com/interview"
OUT_DIR="$TASK_MANAGER_DIR/out"
TEST_DATA_DIR="$TASK_MANAGER_DIR/test-data"

mkdir -p "$OUT_DIR"

if ! javac -d "$OUT_DIR" \
    "$SRC_DIR/Task.java" \
    "$SRC_DIR/TaskDemo.java" \
    "$SRC_DIR/TaskManager.java" \
    "$SRC_DIR/TaskManagerOopDemo.java"
then
    echo "FAIL: Compilation"
    exit 1
fi

passed=0
failed=0

run_test() {
    local test_name="$1"
    local class_name="$2"
    local expected_file="$3"
    local actual_file="$4"

    if java -cp "$OUT_DIR" "$class_name" > "$actual_file" \
            && diff -u "$expected_file" "$actual_file"
    then
        echo "PASS: $test_name"
        passed=$((passed + 1))
    else
        echo "FAIL: $test_name"
        failed=$((failed + 1))
    fi
}

run_test \
    "TaskDemo" \
    "com.interview.TaskDemo" \
    "$TEST_DATA_DIR/task-demo-expected.txt" \
    "$OUT_DIR/task-demo-actual.txt"

run_test \
    "TaskManagerOopDemo" \
    "com.interview.TaskManagerOopDemo" \
    "$TEST_DATA_DIR/task-manager-oop-expected.txt" \
    "$OUT_DIR/task-manager-oop-actual.txt"

printf '\nResult: %d passed, %d failed\n' \
    "$passed" "$failed"

if ((failed > 0)); then
    exit 1
fi