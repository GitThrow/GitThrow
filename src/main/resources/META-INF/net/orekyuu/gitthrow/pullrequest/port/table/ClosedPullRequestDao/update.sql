UPDATE
  closed_pull_request
SET
  `title` = /* pr.title */'',
  `description` = /* pr.description */'',
  `reviewer` = /* pr.reviewer */''
WHERE
  pr_num = /* pr.prNum */0
  AND
  project = /* pr.project */''