package net.orekyuu.gitthrow.pullrequest.port.table;

import org.seasar.doma.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "pr_comment")
public class PullRequestCommentTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;
    @Column(name = "project")
    private final String projectId;
    @Column(name = "pr_num")
    private final Long prNum;
    @Column(name = "text")
    private final String text;
    @Column(name = "created_at")
    private final LocalDateTime createdAt;
    @Column(name = "user")
    private final String userId;

    public PullRequestCommentTable(Long id, String projectId, Long prNum, String text, LocalDateTime createdAt, String userId) {
        this.id = id;
        this.projectId = projectId;
        this.prNum = prNum;
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

    public Long getPrNum() {
        return prNum;
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
        PullRequestCommentTable that = (PullRequestCommentTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PullRequestCommentTable{" +
            "id=" + id +
            ", projectId='" + projectId + '\'' +
            ", prNum=" + prNum +
            ", text='" + text + '\'' +
            ", createdAt=" + createdAt +
            ", userId='" + userId + '\'' +
            '}';
    }
}
