package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "artifact")
public class Artifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public final Integer id;
    @Column(name = "project")
    public final String projectId;
    @Column(name = "file_name")
    public final String fileName;

    public Artifact(Integer id, String projectId, String fileName) {
        this.id = id;
        this.projectId = projectId;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(id, artifact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
