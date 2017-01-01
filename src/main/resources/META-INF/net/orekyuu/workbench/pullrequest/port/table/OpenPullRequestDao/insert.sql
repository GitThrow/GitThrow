INSERT INTO open_pull_request (
  `project`,
  `pr_num`,
  `title`,
  `description`,
  `reviewer`,
  `proponent`,
  `base_branch`,
  `target_branch`
)
VALUES (
  /* pr.project */'project',
  (
    SELECT
      `ticket_count`
    FROM
      `ticket_number`
    WHERE
      ticket_number.project = /* pr.project */'project'
  ),
  /* pr.title */'title',
  /* pr.description */'description',
  /* pr.reviewer */'reviewer',
  /* pr.proponent */'proponent',
  /* pr.baseBranch */'baseBranch',
  /* pr.targetBranch */'targetBranch'
);