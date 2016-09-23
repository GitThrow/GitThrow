package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

@Entity
@Table(name = "test_log")
public class TestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String projectId;
    @Column(name = "log")
    public String log;

    public TestLog(String projectId, String log) {
        this.projectId = projectId;
        this.log = log;
    }

    public TestLog() {
    }
}
