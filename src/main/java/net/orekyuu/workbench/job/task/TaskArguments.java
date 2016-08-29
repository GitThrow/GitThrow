package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.entity.User;

import java.util.UUID;

/**
 * タスクに与えられる引数
 */
public final class TaskArguments {

    private final UUID jobId;
    private final String projectId;
    private final User user;

    public TaskArguments(UUID jobId, String projectId, User user) {
        this.jobId = jobId;
        this.projectId = projectId;
        this.user = user;
    }

    /**
     * @return ジョブ固有のID
     */
    public UUID getJobId() {
        return jobId;
    }

    /**
     * @return 実行対象のプロジェクトID
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @return 実行したユーザー
     */
    public User getUser() {
        return user;
    }
}
