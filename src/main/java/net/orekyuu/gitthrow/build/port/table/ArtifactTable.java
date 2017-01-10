package net.orekyuu.gitthrow.build.port.table;

import org.seasar.doma.*;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "artifact")
public class ArtifactTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Integer id;
    @Column(name = "project")
    private final String projectId;
    @Column(name = "file_name")
    private final String fileName;

    public ArtifactTable(Integer id, String projectId, String fileName) {
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
        ArtifactTable artifactTable = (ArtifactTable) o;
        return Objects.equals(id, artifactTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
