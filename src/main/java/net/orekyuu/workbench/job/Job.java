package net.orekyuu.workbench.job;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.job.task.Task;
import net.orekyuu.workbench.job.task.TaskArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Workbenchで行うジョブの親
 */
public abstract class Job {

    private List<Task> taskList = new LinkedList<>();
    private String projectId;
    private User user;
    private UUID jobId;
    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    /**
     * ジョブの初期化。ジョブを開始する前にかならず呼び出す必要があります。
     * @param projectId プロジェクトID
     * @param user 実行するユーザー
     */
    public final void init(String projectId, User user) {
        if (this.projectId != null) {
            throw new IllegalStateException("すでに初期化済み");
        }

        this.projectId = projectId;
        this.user = user;
        jobId = UUID.randomUUID();
        onInit();
    }

    /**
     * initメソッドが呼び出されるタイミングで実行される。
     * 子クラスでタスクを登録する場合はオーバーライドしてください。
     */
    protected void onInit() {

    }

    /**
     * タスクの実行中に例外が発生した場合コールされます
     * @param e タスクの実行中に発生した例外
     */
    protected void onTaskException(Throwable e) {
        e.printStackTrace();
    }

    /**
     * タスクを追加する
     * @param task 追加するタスク
     */
    protected final void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * ジョブを開始します。ジョブヲ開始する前に{@link #init(String, User)}を呼び出してください。
     * @param emitter
     */
    @Async
    public void start(SseEmitter emitter) {

        JobMessenger messenger = new JobMessenger(emitter, getJobId(), e -> {});
        TaskArguments args = new TaskArguments(jobId, projectId, user);

        for (Task task : taskList) {
            try {
                task.process(messenger, args);
            } catch (Exception e) {
                onTaskException(e);
                break;
            }
        }

        emitter.complete();
    }

    /**
     * @return ジョブが実行されるプロジェクトID
     */
    public final String getProjectId() {
        return projectId;
    }

    /**
     * @return ジョブを実行するユーザー
     */
    public final User getUser() {
        return user;
    }

    /**
     * @return ジョブ固有のID
     */
    public final UUID getJobId() {
        return jobId;
    }
}
