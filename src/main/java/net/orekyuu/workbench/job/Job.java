package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.message.JobState;
import net.orekyuu.workbench.job.message.JobStateMessage;
import net.orekyuu.workbench.job.task.Task;
import net.orekyuu.workbench.job.task.TaskArguments;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.user.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Workbenchで行うジョブの親
 */
public abstract class Job {

    private List<Task> taskList = new LinkedList<>();
    private Project project;
    private User user;
    private UUID jobId;
    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    /**
     * ジョブの初期化。ジョブを開始する前にかならず呼び出す必要があります。
     * @param project プロジェクト
     * @param user 実行するユーザー
     */
    private final void init(Project project, User user) {
        if (this.project != null) {
            throw new IllegalStateException("すでに初期化済み");
        }

        this.project = project;
        this.user = user;
        this.jobId = UUID.randomUUID();
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
     * ジョブを開始します。
     * @param emitter SseEmitter
     * @param project プロジェクトID
     * @param user ジョブを実行するユーザー
     */
    @Async
    public Future<Void> start(SseEmitter emitter, Project project, User user) {
        init(project, user);
        emitter.onTimeout(() -> {
            System.out.println("timeout");
        });
        JobMessenger messenger = new JobMessenger(emitter, getJobId(), e -> {
            System.out.println(e);
        });
        TaskArguments args = new TaskArguments(jobId, project, user);

        messenger.send(new JobStateMessage(JobState.START));

        JobState result = JobState.SUCCESS;
        String message = null;
        for (Task task : taskList) {
            try {
                boolean next = task.process(messenger, args);
                //falseが返ったら次のタスクを実行しない
                if (!next) {
                    break;
                }
            } catch (JobContinuesImpossibleException e) {
                onTaskException(e);
                result = JobState.ERROR;
                message = e.getErrorMessage();
                break;
            } catch (Exception e) {
                onTaskException(e);
                break;
            }
        }

        messenger.send(new JobStateMessage(result, message));

        emitter.complete();
        return new AsyncResult<>(null);
    }

    /**
     * @return ジョブが実行されるプロジェクト
     */
    protected final Project getProject() {
        return project;
    }

    /**
     * @return ジョブを実行するユーザー
     */
    protected final User getUser() {
        return user;
    }

    /**
     * @return ジョブ固有のID
     */
    protected final UUID getJobId() {
        return jobId;
    }
}
