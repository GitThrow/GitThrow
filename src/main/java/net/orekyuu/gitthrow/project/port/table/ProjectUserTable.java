package net.orekyuu.gitthrow.project.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "project_user")
public class ProjectUserTable {
    @Id
    @Column(name = "project")
    private final String project;
    @Id
    @Column(name = "user")
    private final String user;

    public ProjectUserTable(String project, String user) {
        this.project = project;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ProjectUserTable{" +
            "project='" + project + '\'' +
            ", user='" + user + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectUserTable that = (ProjectUserTable) o;
        return Objects.equals(project, that.project) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, user);
    }
}
