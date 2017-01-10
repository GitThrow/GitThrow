package net.orekyuu.gitthrow.project.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "projects")
public class ProjectTable {
    @Id
    @Column(name = "project_id")
    private final String id;
    @Column(name = "project_name", updatable = true)
    private final String name;
    @Column(name = "owner")
    private final String ownerUserId;

    public ProjectTable(String id, String name, String ownerUserId) {
        this.id = id;
        this.name = name;
        this.ownerUserId = ownerUserId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTable that = (ProjectTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
