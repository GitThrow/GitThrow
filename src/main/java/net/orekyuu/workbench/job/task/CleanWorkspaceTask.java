package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ワークスペースを削除するタスク
 */
@Component
@Scope("prototype")
public class CleanWorkspaceTask implements Task {

    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        jobWorkspaceService.deleteWorkspaceIfExists(args.getJobId());
        //ワークスペースを消したので次のタスクは必ず実行しない
        return false;
    }
}
