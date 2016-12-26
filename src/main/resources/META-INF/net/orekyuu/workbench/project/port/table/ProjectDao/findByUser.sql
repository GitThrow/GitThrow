SELECT
  projects.project_id,
  projects.project_name,
  projects.owner
FROM
  projects
  INNER JOIN project_user on projects.project_id = project_user.project
WHERE
  project_user.user = /* userId */'user'