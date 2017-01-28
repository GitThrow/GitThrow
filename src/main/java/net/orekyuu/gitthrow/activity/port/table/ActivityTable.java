package net.orekyuu.gitthrow.activity.port.table;

import org.seasar.doma.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "activities")
public class ActivityTable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Integer id;
    @Column(name = "title")
    private final String title;
    @Column(name = "description")
    private final String description;
    @Column(name = "user")
    private final String userId;
    @Column(name = "project")
    private final String projectId;
    @Column(name = "created_at")
    private final LocalDateTime createdAt;

    public ActivityTable(Integer id, String title, String description, String userId, String projectId, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityTable that = (ActivityTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActivityTable{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", projectId='").append(projectId).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
