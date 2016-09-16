package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.task.CleanWorkspaceTask;
import net.orekyuu.workbench.job.task.GitCloneTask;
import net.orekyuu.workbench.job.task.MergeTask;
import net.orekyuu.workbench.job.task.PushTask;
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
    private CleanWorkspaceTask cleanWorkspaceTask;

    private String baseBranch;
    private String targetBranch;

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
        addTask(cleanWorkspaceTask);
    }

    public void setBaseBranch(String baseBranch) {
        this.baseBranch = baseBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }
}
