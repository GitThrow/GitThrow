package net.orekyuu.gitthrow.activity.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.orekyuu.gitthrow.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Activity {
    private int id;
    private String title;
    private String description;
    private User user;
    private String projectId;
    private LocalDateTime createdAt;

    @JsonCreator
    public Activity(
        @JsonProperty("id") int id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("user") User user,
        @JsonProperty("projectId") String projectId,
        @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public String getProjectId() {
        return projectId;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Activity{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", user=").append(user);
        sb.append(", projectId='").append(projectId).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
