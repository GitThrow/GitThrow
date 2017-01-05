SELECT
  users.id,
  users.name,
  users.password,
  users.admin
FROM
  `users`
  INNER JOIN `project_user` ON users.id = project_user.user
WHERE
  project_user.project = /* projectId */'prj'