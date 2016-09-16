package net.orekyuu.workbench.service;

public class PullRequestModel {
    private int number;
    private String title;
    private String description;
    private String reviewer;
    private String proponent;
    private String baseBranch;
    private String targetBranch;
    private boolean conflict;

    public PullRequestModel(int number, String title, String description, String reviewer, String proponent, String baseBranch, String targetBranch) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.reviewer = reviewer;
        this.proponent = proponent;
        this.baseBranch = baseBranch;
        this.targetBranch = targetBranch;
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
            '}';
    }
}
