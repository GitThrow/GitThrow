package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "test_settings")
public class TestSettings {

    @Id
    @Column(name = "project")
    public String projectId;
    @Column(name = "test_command")
    public String testCommand;
    @Column(name = "xml_path")
    public String xmlPath;
}
