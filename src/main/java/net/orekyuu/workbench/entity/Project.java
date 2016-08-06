package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @Column(name = "project_id")
    public String id;
    @Column(name = "project_name", updatable = true)
    public String name;
    @Column(name = "owner")
    public String ownerUserId;

    public Project(String id, String name, String ownerUserId) {
        this.id = id;
        this.name = name;
        this.ownerUserId = ownerUserId;
    }

    public Project() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
