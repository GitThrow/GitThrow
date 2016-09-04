package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

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

    public Artifact(String projectId, String fileName) {
        this.id = null;
        this.projectId = projectId;
        this.fileName = fileName;
    }
}
