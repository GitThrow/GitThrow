package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.task.CleanWorkspaceTask;
import net.orekyuu.workbench.job.task.GitCloneTask;
import net.orekyuu.workbench.job.task.SaveArtifactTask;
import net.orekyuu.workbench.job.task.TestTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TestJob extends Job {

    @Autowired
    private TestTask testTask;
    @Autowired
    private SaveArtifactTask artifactTask;
    @Autowired
    private GitCloneTask cloneTask;
    @Autowired
    private CleanWorkspaceTask cleanWorkspaceTask;

    private String hash = "master";

    @Override
    protected void onInit() {
        cloneTask.setBranch(hash);
        addTask(cloneTask);
        addTask(testTask);
        addTask(cleanWorkspaceTask);
    }

    /**
     * ビルドするコミットを指定します
     * @param hash ビルドするコミットのハッシュ
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

}
