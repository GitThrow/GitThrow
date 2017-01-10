package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.user.domain.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * タスクに与えられる引数
 */
public final class TaskArguments {

    private final UUID jobId;
    private final Project project;
    private final User user;
    private final Map<String, Object> taskDataMap = new HashMap<>();

    public TaskArguments(UUID jobId, Project project, User user) {
        this.jobId = jobId;
        this.project = project;
        this.user = user;
    }

    /**
     * @return ジョブ固有のID
     */
    public UUID getJobId() {
        return jobId;
    }

    /**
     * @return 実行対象のプロジェクト
     */
    public Project getProject() {
        return project;
    }

    /**
     * @return 実行したユーザー
     */
    public User getUser() {
        return user;
    }


    public void putData(String key, Object value) {
        taskDataMap.put(key, value);
    }

    public <T> Optional<T> getData(String key) {
        Object o = taskDataMap.get(key);
        return Optional.ofNullable((T) o);
    }
}
