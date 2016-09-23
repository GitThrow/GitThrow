SELECT
  id,
  project,
  created_at,
  status,
  commit
FROM
  test_log
WHERE
  project = /*projectId*/''