package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "build_settings")
public class BuildSettings {

    @Id
    @Column(name = "project")
    public String projectId;
    @Column(name = "build_command")
    public String buildCommand;
    @Column(name = "artifact_path")
    public String artifactPath;
}
