package net.orekyuu.gitthrow.build.port.table;

import net.orekyuu.gitthrow.build.model.domain.TestStatus;
import org.seasar.doma.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(immutable = true)
@Table(name = "test_log")
public class TestLogTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;
    @Column(name = "project")
    private final String projectId;
    @Column(name = "log")
    private final String log;
    @Column(name = "created_at")
    private final LocalDateTime createdAt;
    @Column(name = "status")
    private final TestStatus status;
    @Column(name = "commit")
    private final String commit;

    public TestLogTable(Long id, String projectId, String log, LocalDateTime createdAt, TestStatus status, String commit) {
        this.id = id;
        this.projectId = projectId;
        this.log = log;
        this.createdAt = createdAt;
        this.status = status;
        this.commit = commit;
    }

    public Long getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getLog() {
        return log;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TestStatus getStatus() {
        return status;
    }

    public String getCommit() {
        return commit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestLogTable that = (TestLogTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
