package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

import java.time.LocalDateTime;

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
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    @Column(name = "status")
    public TestStatus status;
    @Column(name = "commit")
    public String commit;

    public TestLog(String projectId, String log, LocalDateTime createdAt, TestStatus testStatus, String commit) {
        this.projectId = projectId;
        this.log = log;
        this.createdAt = createdAt;
        this.status = testStatus;
        this.commit =commit;
    }

    public TestLog() {
    }
}
