package net.orekyuu.gitthrow.controller.rest.model;

public class PullRequestModel {
    private int number;
    private String title;
    private String description;
    private String reviewer;
    private String proponent;
    private String baseBranch;
    private String targetBranch;
    private boolean conflict;
    private boolean closed;

    public PullRequestModel(int number, String title, String description, String reviewer, String proponent, String baseBranch, String targetBranch, boolean closed) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.reviewer = reviewer;
        this.proponent = proponent;
        this.baseBranch = baseBranch;
        this.targetBranch = targetBranch;
        this.closed = closed;
    }

    public int getNumber() {
        return number;
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

    public boolean isConflict() {
        return conflict;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "PullRequestModel{" +
            "number=" + number +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", reviewer='" + reviewer + '\'' +
            ", proponent='" + proponent + '\'' +
            ", baseBranch='" + baseBranch + '\'' +
            ", targetBranch='" + targetBranch + '\'' +
            ", conflict=" + conflict +
            ", closed=" + closed +
            '}';
    }
}
