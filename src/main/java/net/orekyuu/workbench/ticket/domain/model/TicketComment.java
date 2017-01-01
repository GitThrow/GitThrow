package net.orekyuu.workbench.ticket.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.orekyuu.workbench.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class TicketComment {

    private final int id;
    private final String projectId;
    private final String text;
    private final LocalDateTime createdAt;
    private final User user;

    public TicketComment(int id, String projectId, String text, LocalDateTime createdAt, User user) {
        this.id = id;
        this.projectId = projectId;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketComment that = (TicketComment) o;
        return id == that.id &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TicketComment{");
        sb.append("id=").append(id);
        sb.append(", projectId='").append(projectId).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
