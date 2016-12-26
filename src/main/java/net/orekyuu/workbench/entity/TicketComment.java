package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "ticket_comment")
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;
    @Column(name = "project")
    private final String projectId;
    @Column(name = "ticket_num")
    private final Long ticketNum;
    @Column(name = "text")
    private final String text;
    @Column(name = "created_at")
    private final LocalDateTime createdAt;
    @Column(name = "user")
    private final String userId;

    public TicketComment(Long id, String projectId, Long ticketNum, String text, LocalDateTime createdAt, String userId) {
        this.id = id;
        this.projectId = projectId;
        this.ticketNum = ticketNum;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public Long getTicketNum() {
        return ticketNum;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketComment that = (TicketComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
