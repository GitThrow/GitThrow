package net.orekyuu.gitthrow.pullrequest.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.orekyuu.gitthrow.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class PullRequestComment {
    private final int id;
    private final String projectId;
    private final String text;
    private final LocalDateTime createdAt;
    private final User user;

    public PullRequestComment(
        @JsonProperty("id")int id,
        @JsonProperty("projectId") String projectId,
        @JsonProperty("text") String text,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("user") User user) {
        this.id = id;
        this.projectId = projectId;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getText() {
        return text;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "PullRequestComment{" +
            "id=" + id +
            ", projectId='" + projectId + '\'' +
            ", text='" + text + '\'' +
            ", createdAt=" + createdAt +
            ", user=" + user +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullRequestComment that = (PullRequestComment) o;
        return id == that.id &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId);
    }
}
