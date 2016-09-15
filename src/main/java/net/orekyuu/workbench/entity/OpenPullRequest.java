package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

@Entity
@Table(name = "open_pull_request")
public class OpenPullRequest {
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
    @Column(name = "base_branch")
    public String baseBranch;
    @Column(name = "target_branch")
    public String targetBranch;
}
