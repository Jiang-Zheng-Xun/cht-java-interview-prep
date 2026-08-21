#!/usr/bin/env bash

set -uo pipefail

SCRIPT_DIR="$(
    cd "$(dirname "${BASH_SOURCE[0]}")"
    pwd
)"
DAY_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
SOURCE_FILE="$DAY_DIR/JavaExceptionHandlingTryCatch.java"
TEST_DATA_DIR="$DAY_DIR/test-data"
OUT_DIR="$DAY_DIR/out"

mkdir -p "$OUT_DIR"

if ! javac -d "$OUT_DIR" "$SOURCE_FILE"; then
    echo "FAIL: Compilation"
    exit 1
fi

shopt -s nullglob

input_files=(
    "$TEST_DATA_DIR"/java-exception-*-input.txt
)

if ((${#input_files[@]} == 0)); then
    echo "FAIL: No input fixtures found."
    exit 1
fi

passed=0
failed=0

for input_file in "${input_files[@]}"; do
    test_name="$(
        basename "$input_file" -input.txt
    )"
    expected_file="${input_file%-input.txt}-expected.txt"
    actual_file="$OUT_DIR/${test_name}-actual.txt"

    if [[ ! -f "$expected_file" ]]; then
        echo "FAIL: $test_name"
        echo "Missing expected file: $expected_file"
        failed=$((failed + 1))
        continue
    fi

    if ! java -cp "$OUT_DIR" \
            JavaExceptionHandlingTryCatch \
            < "$input_file" \
            > "$actual_file"
    then
        echo "FAIL: $test_name"
        echo "Program execution failed."
        failed=$((failed + 1))
        continue
    fi

    diff_output="$(
        diff -u "$expected_file" "$actual_file"
    )"
    diff_status=$?

    if ((diff_status == 0)); then
        echo "PASS: $test_name"
        passed=$((passed + 1))
    else
        echo "FAIL: $test_name"
        echo "Expected: $expected_file"
        echo "Actual:   $actual_file"
        printf '%s\n' "$diff_output"
        failed=$((failed + 1))
    fi
done

printf '\nResult: %d passed, %d failed\n' \
    "$passed" "$failed"

if ((failed > 0)); then
    exit 1
fi