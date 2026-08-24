#!/usr/bin/env bash

set -u

script_directory="$(
    cd "$(dirname "${BASH_SOURCE[0]}")"
    pwd
)"

day_directory="$(
    cd "${script_directory}/.."
    pwd
)"

source_file="${day_directory}/JavaSort.java"
test_data_directory="${day_directory}/test-data"
output_directory="${day_directory}/out"

mkdir -p "$output_directory"

if ! javac \
    -d "$output_directory" \
    "$source_file"
then
    echo "FAIL: Java Sort compilation"
    exit 1
fi

passed_count=0
failed_count=0

for case_name in sample ties boundary
do
    input_file="${test_data_directory}/java-sort-${case_name}-input.txt"
    expected_file="${test_data_directory}/java-sort-${case_name}-expected.txt"
    actual_file="${output_directory}/java-sort-${case_name}-actual.txt"

    if ! java \
        -cp "$output_directory" \
        JavaSort \
        < "$input_file" \
        >| "$actual_file"
    then
        echo "FAIL: java-sort-${case_name} execution"
        failed_count=$((failed_count + 1))
        continue
    fi

    if diff -u \
        "$expected_file" \
        "$actual_file"
    then
        echo "PASS: java-sort-${case_name}"
        passed_count=$((passed_count + 1))
    else
        echo "FAIL: java-sort-${case_name}"
        failed_count=$((failed_count + 1))
    fi
done

echo
echo "Result: ${passed_count} passed, ${failed_count} failed"

if [ "$failed_count" -ne 0 ]
then
    exit 1
fi

exit 0