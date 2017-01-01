package net.orekyuu.workbench.build.model.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class TestLog {
    private final Long id;
    private final String projectId;
    private final String log;
    private final LocalDateTime createdAt;
    private final TestStatus status;
    private final String commit;

    public TestLog(Long id, String projectId, String log, LocalDateTime createdAt, TestStatus status, String commit) {
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
        TestLog testLog = (TestLog) o;
        return Objects.equals(id, testLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestLog{");
        sb.append("id=").append(id);
        sb.append(", projectId='").append(projectId).append('\'');
        sb.append(", log='").append(log).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", status=").append(status);
        sb.append(", commit='").append(commit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
