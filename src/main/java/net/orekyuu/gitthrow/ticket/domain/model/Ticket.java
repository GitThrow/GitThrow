package net.orekyuu.gitthrow.ticket.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.orekyuu.gitthrow.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {

    private final String projectId;
    private int ticketNum;
    private String title;
    private String description;
    private User assignee;
    private User proponent;
    private LocalDateTime limit;
    private TicketType type;
    private TicketStatus status;
    private TicketPriority priority;

    @JsonCreator
    public Ticket(@JsonProperty("project") String projectId,
                  @JsonProperty("ticketNum") int ticketNum,
                  @JsonProperty("title") String title,
                  @JsonProperty("description") String description,
                  @JsonProperty("assignee") User assignee,
                  @JsonProperty("proponent") User proponent,
                  @JsonProperty("limit") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime limit,
                  @JsonProperty("type") TicketType type,
                  @JsonProperty("status") TicketStatus status,
                  @JsonProperty("priority") TicketPriority priority) {
        this.projectId = projectId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public void setProponent(User proponent) {
        this.proponent = proponent;
    }

    public void setLimit(LocalDateTime limit) {
        this.limit = limit;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
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

    public User getAssignee() {
        return assignee;
    }

    public User getProponent() {
        return proponent;
    }

    public LocalDateTime getLimit() {
        return limit;
    }

    public TicketType getType() {
        return type;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketNum == ticket.ticketNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNum);
    }
}
