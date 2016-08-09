INSERT INTO open_tickets (
  `project`,
  `ticket_num`,
  `title`,
  `description`,
  `assignee`,
  `proponent`,
  `limit`,
  `type`,
  `status`,
  `priority`
)
VALUES (
  /* ticket.project */'project',
  (
    SELECT
      `ticket_count`
    FROM
      `ticket_number`
    WHERE
      ticket_number.project = /* ticket.project */'project'
  ),
  /* ticket.title */'title',
  /* ticket.description */'description',
  /* ticket.assignee */'assignee',
  /* ticket.proponent */'proponent',
  /* ticket.limit */NULL ,
  /* ticket.type */0,
  /* ticket.status */0,
  /* ticket.priority */0
);