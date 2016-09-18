package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

@Entity
@Table(name = "closed_pull_request")
public class ClosedPullRequest {
    @Column(name = "project")
    public String project;
    @Column(name = "pr_num")
    public int prNum;
    @Column(name = "title")
    public String title;
    @Column(name = "description")
    public String description;
    @Column(name = "reviewer")
    public String reviewer;
    @Column(name = "proponent")
    public String proponent;
    @Column(name = "base_commit")
    public String baseCommit;
    @Column(name = "target_commit")
    public String targetCommit;
}
