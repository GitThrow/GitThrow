package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = false)
@Table(name = "open_pull_request")
public class OpenPullRequest {
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
    @Column(name = "base_branch")
    private final String baseBranch;
    @Column(name = "target_branch")
    private final String targetBranch;

    public OpenPullRequest(String project, Long prNum, String title, String description, String reviewer, String proponent, String baseBranch, String targetBranch) {
        this.project = project;
        this.prNum = prNum;
        this.title = title;
        this.description = description;
        this.reviewer = reviewer;
        this.proponent = proponent;
        this.baseBranch = baseBranch;
        this.targetBranch = targetBranch;
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

    public String getBaseBranch() {
        return baseBranch;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenPullRequest that = (OpenPullRequest) o;
        return Objects.equals(project, that.project) &&
            Objects.equals(prNum, that.prNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, prNum);
    }
}
