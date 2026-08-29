-- HackerRank: Revising the Select Query I
-- Dialect: MySQL
--
-- Query all columns for American cities with populations
-- strictly larger than 100000.

SELECT
    ID,
    NAME,
    COUNTRYCODE,
    DISTRICT,
    POPULATION
FROM CITY
WHERE POPULATION > 100000
  AND COUNTRYCODE = 'USA';