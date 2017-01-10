package net.orekyuu.gitthrow.ticket.port.table;


import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "open_tickets")
public class OpenTicketTable {
    @Column(name = "project")
    private final String project;
    @Column(name = "ticket_num")
    private final int ticketNum;
    @Column(name = "title")
    private final String title;
    @Column(name = "description")
    private final String description;
    @Column(name = "assignee")
    private final String assignee;
    @Column(name = "proponent")
    private final String proponent;
    @Column(name = "limit")
    private final LocalDateTime limit;
    @Column(name = "type")
    private final int type;
    @Column(name = "status")
    private final int status;
    @Column(name = "priority")
    private final int priority;

    public OpenTicketTable(String project, int ticketNum, String title, String description, String assignee, String proponent, LocalDateTime limit, int type, int status, int priority) {
        this.project = project;
        this.ticketNum = ticketNum;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.proponent = proponent;
        this.limit = limit;
        this.type = type;
        this.status = status;
        this.priority = priority;
    }

    public String getProject() {
        return project;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getProponent() {
        return proponent;
    }

    public LocalDateTime getLimit() {
        return limit;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTicketTable that = (OpenTicketTable) o;
        return ticketNum == that.ticketNum &&
            Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, ticketNum);
    }
}
