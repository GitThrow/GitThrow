package net.orekyuu.gitthrow.theme.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "project_theme")
public class ProjectThemeTable {
    @Id
    @Column(name = "project_id")
    private final String projectId;
    @Column(name = "opacity")
    private final double opacity;
    @Column(name = "image")
    private final byte[] image;
    @Column(name = "updated_at")
    private final LocalDateTime updatedAt;

    public ProjectThemeTable(String projectId, double opacity, byte[] image, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.opacity = opacity;
        this.image = image;
        this.updatedAt = updatedAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public double getOpacity() {
        return opacity;
    }

    public byte[] getImage() {
        return image;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectThemeTable that = (ProjectThemeTable) o;
        return Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }
}
