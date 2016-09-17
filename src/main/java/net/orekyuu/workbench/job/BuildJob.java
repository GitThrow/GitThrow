package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.task.BuildTask;
import net.orekyuu.workbench.job.task.CleanWorkspaceTask;
import net.orekyuu.workbench.job.task.GitCloneTask;
import net.orekyuu.workbench.job.task.SaveArtifactTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private String branch = "master";

    @Override
    protected void onInit() {
        cloneTask.setBranch(branch);
        addTask(cloneTask);
        addTask(buildTask);
        addTask(artifactTask);
        addTask(cleanWorkspaceTask);
    }

    /**
     * ビルドするブランチを指定します
     * @param branch ビルドするブランチ
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
}
