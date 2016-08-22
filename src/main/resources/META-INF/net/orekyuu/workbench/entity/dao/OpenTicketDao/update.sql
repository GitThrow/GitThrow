UPDATE
  open_tickets
SET
  `title` = /* ticket.title */'',
  `description` = /* ticket.description */'',
  `assignee` = /* ticket.assignee */'',
  `limit` = /* ticket.limit */NULL ,
  `type` = /* ticket.type */0,
  `status` = /* ticket.status */0,
  `priority` = /*ticket.priority */0
WHERE
  ticket_num = /* ticket.ticketNum */0
AND
  project = /* ticket.project */''