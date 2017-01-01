package net.orekyuu.workbench.pullrequest.domain.model;

import net.orekyuu.workbench.user.domain.model.User;

import java.util.Objects;

public class PullRequest {

    private final String projectId;
    private final int pullrequestNum;
    private String title;
    private String description;
    private User reviewer;
    private final User proponent;
    private final String base;
    private final String target;
    private PullRequestState state;


    public PullRequest(String projectId, int pullrequestNum, String title, String description, User reviewer, User proponent, String base, String target, PullRequestState state) {
        this.projectId = projectId;
        this.pullrequestNum = pullrequestNum;
        this.title = title;
        this.description = description;
        this.reviewer = reviewer;
        this.proponent = proponent;
        this.base = base;
        this.target = target;
        this.state = state;
    }

    public String getProjectId() {
        return projectId;
    }

    public int getPullrequestNum() {
        return pullrequestNum;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getProponent() {
        return proponent;
    }

    public String getBase() {
        return base;
    }

    public String getTarget() {
        return target;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }


    public PullRequestState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullRequest that = (PullRequest) o;
        return pullrequestNum == that.pullrequestNum &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, pullrequestNum);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PullRequest{");
        sb.append("projectId='").append(projectId).append('\'');
        sb.append(", pullrequestNum=").append(pullrequestNum);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", reviewer=").append(reviewer);
        sb.append(", proponent=").append(proponent);
        sb.append(", base='").append(base).append('\'');
        sb.append(", target='").append(target).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
