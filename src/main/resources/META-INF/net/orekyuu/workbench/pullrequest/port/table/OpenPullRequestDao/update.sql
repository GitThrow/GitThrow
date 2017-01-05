UPDATE
  open_pull_request
SET
  `title` = /* pr.title */'',
  `description` = /* pr.description */'',
  `reviewer` = /* pr.reviewer */'',
  `proponent` = /* pr.proponent */'',
  `base_branch` = /* pr.baseBranch */'',
  `target_branch` = /* pr.targetBranch */''
WHERE
  pr_num = /* pr.prNum */0
  AND
  project = /* pr.project */''