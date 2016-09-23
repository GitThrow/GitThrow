package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comment")
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String projectId;
    @Column(name = "ticket_num")
    public int ticketNum;
    @Column(name = "text")
    public String text;
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    @Column(name = "user")
    public String userId;

    public TicketComment(String projectId, int ticketNum, String text, LocalDateTime createdAt, String userId) {
        this.projectId = projectId;
        this.ticketNum = ticketNum;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public TicketComment() {
    }
}
