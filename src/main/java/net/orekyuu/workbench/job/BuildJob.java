package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.task.BuildTask;
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

    @Override
    protected void onInit() {
        cloneTask.setBranch("master");
        addTask(cloneTask);
        addTask(buildTask);
        addTask(artifactTask);
    }
}
