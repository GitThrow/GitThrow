package net.orekyuu.workbench.ticket.util;

import net.orekyuu.workbench.ticket.domain.model.*;
import net.orekyuu.workbench.ticket.port.table.*;
import net.orekyuu.workbench.user.domain.model.User;

public class TicketUtil {

    public static Ticket fromTable(OpenTicketTable table, TicketTypeTable typeTable, TicketStatusTable statusTable, TicketPriorityTable priorityTable, User assignee, User proponent) {
        return new Ticket(
            table.getProject(),
            table.getTicketNum(),
            table.getTitle(),
            table.getDescription(),
            assignee,
            proponent,
            table.getLimit(),
            ticketTypeFromTable(typeTable),
            ticketStatusFromTable(statusTable),
            ticketPriorityFromTable(priorityTable));
    }

    public static Ticket fromTable(OpenTicketTable table, TicketTypeTable typeTable, TicketStatusTable statusTable, TicketPriorityTable priorityTable) {
        return new Ticket(
            table.getProject(),
            table.getTicketNum(),
            table.getTitle(),
            table.getDescription(),
            null,
            null,
            table.getLimit(),
            ticketTypeFromTable(typeTable),
            ticketStatusFromTable(statusTable),
            ticketPriorityFromTable(priorityTable));
    }

    public static Ticket fromTable(OpenTicketTable table) {
        return new Ticket(
            table.getProject(),
            table.getTicketNum(),
            table.getTitle(),
            table.getDescription(),
            null,
            null,
            table.getLimit(),
            null,
            null,
            null);
    }

    public static TicketComment commentFromTable(TicketCommentTable table, User user) {
        return new TicketComment(table.getId().intValue(), table.getProjectId(), table.getText(), table.getCreatedAt(), user);
    }

    public static TicketStatus ticketStatusFromTable(TicketStatusTable table) {
        return new TicketStatus(table.getId().intValue(), table.getStatus());
    }

    public static TicketType ticketTypeFromTable(TicketTypeTable table) {
        return new TicketType(table.getId().intValue(), table.getType());
    }

    public static TicketPriority ticketPriorityFromTable(TicketPriorityTable table) {
        return new TicketPriority(table.getId().intValue(), table.getPriority());
    }
}
