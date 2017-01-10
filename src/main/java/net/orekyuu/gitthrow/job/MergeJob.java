package net.orekyuu.gitthrow.job;

import net.orekyuu.gitthrow.job.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope("prototype")
public class MergeJob extends Job {

    @Autowired
    private GitCloneTask cloneTask;
    @Autowired
    private MergeTask mergeTask;
    @Autowired
    private PushTask pushTask;
    @Autowired
    private ClosePullRequestTask closePullRequestTask;
    @Autowired
    private BuildTask buildTask;
    @Autowired
    private SaveArtifactTask artifactTask;
    @Autowired
    private TestTask testTask;
    @Autowired
    private SaveTestLogTask testLogTask;

    @Autowired
    private MergeJobCommentTask commentTask;

    @Autowired
    private CleanWorkspaceTask cleanWorkspaceTask;

    private String baseBranch;
    private String targetBranch;
    private int prNum = -1;

    private boolean enableBuild = true;
    private boolean enableTest = true;

    @Override
    protected void onInit() {
        Objects.requireNonNull(baseBranch, "baseBranch is null");
        Objects.requireNonNull(targetBranch, "targetBranch is null");

        cloneTask.setBranch(baseBranch);
        mergeTask.setBaseBranch(baseBranch);
        mergeTask.setTargetBranch(targetBranch);

        addTask(cloneTask);
        addTask(mergeTask);
        addTask(pushTask);
        if (0 < prNum) {
            closePullRequestTask.setPrNum(prNum);
            addTask(closePullRequestTask);

            if (enableBuild) {
                addTask(buildTask);
                addTask(artifactTask);
            }

            if (enableTest) {
                addTask(testTask);
                addTask(testLogTask);
            }

            if (enableBuild || enableTest) {
                commentTask.setCommentTarget(prNum);
                addTask(commentTask);
            }
        }
        addTask(cleanWorkspaceTask);
    }

    public void setBaseBranch(String baseBranch) {
        this.baseBranch = baseBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public void setClosePullRequestNum(int num) {
        prNum = num;
    }

    public void setEnableBuild(boolean enableBuild) {
        this.enableBuild = enableBuild;
    }

    public void setEnableTest(boolean enableTest) {
        this.enableTest = enableTest;
    }
}
