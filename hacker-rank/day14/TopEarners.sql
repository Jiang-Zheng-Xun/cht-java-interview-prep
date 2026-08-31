/*
 * HackerRank: Top Earners
 * Domain: SQL / Aggregation
 * Problem:
 * Find the maximum total earnings, defined as months multiplied
 * by salary, and the number of employees who earned that amount.
 *
 * Expected output:
 * maximum_total_earnings employee_count
 */

-- Paste the accepted HackerRank query below.

SELECT
    (E.months * E.salary) AS earnings,
    COUNT(*) AS employee_count
FROM Employee AS E
GROUP BY
    (E.months * E.salary)
ORDER BY
    earnings DESC
LIMIT 1;