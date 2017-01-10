package net.orekyuu.gitthrow.pullrequest.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "closed_pull_request")
public class ClosedPullRequestTable {
    @Column(name = "project")
    private final String project;
    @Column(name = "pr_num")
    private final Long prNum;
    @Column(name = "title")
    private final String title;
    @Column(name = "description")
    private final String description;
    @Column(name = "reviewer")
    private final String reviewer;
    @Column(name = "proponent")
    private final String proponent;
    @Column(name = "base_commit")
    private final String baseCommit;
    @Column(name = "target_commit")
    private final String targetCommit;

    public ClosedPullRequestTable(String project, Long prNum, String title, String description, String reviewer, String proponent, String baseCommit, String targetCommit) {
        this.project = project;
        this.prNum = prNum;
        this.title = title;
        this.description = description;
        this.reviewer = reviewer;
        this.proponent = proponent;
        this.baseCommit = baseCommit;
        this.targetCommit = targetCommit;
    }

    public String getProject() {
        return project;
    }

    public Long getPrNum() {
        return prNum;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getProponent() {
        return proponent;
    }

    public String getBaseCommit() {
        return baseCommit;
    }

    public String getTargetCommit() {
        return targetCommit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClosedPullRequestTable that = (ClosedPullRequestTable) o;
        return Objects.equals(project, that.project) &&
            Objects.equals(prNum, that.prNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, prNum);
    }
}
