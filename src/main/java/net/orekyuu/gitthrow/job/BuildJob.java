package net.orekyuu.gitthrow.job;

import net.orekyuu.gitthrow.job.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;

@Component
@Scope("prototype")
public class BuildJob extends Job {

    @Autowired
    private BuildTask buildTask;
    @Autowired
    private SaveArtifactTask artifactTask;
    @Autowired
    private GitCloneTask cloneTask;
    @Autowired
    private CleanWorkspaceTask cleanWorkspaceTask;
    @Autowired
    private BuildJobCommentTask commentTask;

    private String hash = "master";
    private OptionalInt pullRequestNum = OptionalInt.empty();

    @Override
    protected void onInit() {
        cloneTask.setBranch(hash);
        addTask(cloneTask);
        addTask(buildTask);
        addTask(artifactTask);
        pullRequestNum.ifPresent(it -> {
            commentTask.setCommentTarget(it);
            addTask(commentTask);
        });
        addTask(cleanWorkspaceTask);
    }

    /**
     * ビルドするコミットを指定します
     * @param hash ビルドするコミットのハッシュ
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * ビルド完了時のコメント先を設定します
     * @param taskNum pullRequestNum
     */
    public void setCommentTarget(int taskNum) {
        pullRequestNum = OptionalInt.of(taskNum);
    }
}
