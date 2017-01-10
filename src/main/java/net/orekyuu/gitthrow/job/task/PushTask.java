package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.job.JobWorkspaceService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class PushTask implements Task {

    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        try (Repository repo = jobWorkspaceService.getRepository(args.getJobId())) {
            Git git = new Git(repo);
            PushCommand pushCommand = git.push().setRemote("origin").setPushAll();
            pushCommand.setProgressMonitor(new SimpleLogProgressMonitor(messenger)).call();
        }
        return true;
    }
}