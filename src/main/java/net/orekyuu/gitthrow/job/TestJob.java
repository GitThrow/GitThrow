package net.orekyuu.gitthrow.job;

import net.orekyuu.gitthrow.job.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;

@Component
@Scope("prototype")
public class TestJob extends Job {

    @Autowired
    private TestTask testTask;
    @Autowired
    private SaveTestLogTask saveTestLogTask;
    @Autowired
    private GitCloneTask cloneTask;
    @Autowired
    private CleanWorkspaceTask cleanWorkspaceTask;
    @Autowired
    private TestJobCommentTask commentTask;

    private String hash = "master";
    private OptionalInt prNum = OptionalInt.empty();

    @Override
    protected void onInit() {
        cloneTask.setBranch(hash);
        addTask(cloneTask);
        addTask(testTask);
        addTask(saveTestLogTask);
        prNum.ifPresent((int num) -> {
            commentTask.setCommentTarget(num);
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

    public void setPrNum(int pr) {
        this.prNum = OptionalInt.of(pr);
    }

}
