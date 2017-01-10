package net.orekyuu.gitthrow.pullrequest.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "pr_number")
public class PullRequestNumberTable {
    @Id
    @Column(name = "project")
    private final String project;
    @Column(name = "pr_count")
    private final Long prCount;

    public PullRequestNumberTable(String project, Long prCount) {
        this.project = project;
        this.prCount = prCount;
    }

    public String getProject() {
        return project;
    }

    public Long getPrCount() {
        return prCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullRequestNumberTable that = (PullRequestNumberTable) o;
        return Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PullRequestNumberTable{");
        sb.append("project='").append(project).append('\'');
        sb.append(", prCount=").append(prCount);
        sb.append('}');
        return sb.toString();
    }
}
