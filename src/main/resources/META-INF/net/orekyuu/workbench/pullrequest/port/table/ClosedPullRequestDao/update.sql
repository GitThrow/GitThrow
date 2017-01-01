UPDATE
  closed_pull_request
SET
  `title` = /* pr.title */'',
  `description` = /* pr.description */'',
  `reviewer` = /* pr.reviewer */'',
  `proponent` = /* pr.proponent */'',
  `base_commit` = /* pr.baseCommit */'',
  `target_commit` = /* pr.targetCommit */''
WHERE
  pr_num = /* pr.prNum */0
  AND
  project = /* pr.project */''