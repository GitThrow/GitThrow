package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import net.orekyuu.workbench.job.message.LogMessage;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class MergeTask implements Task {

    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    private String targetBranch;
    private String baseBranch;

    public static final String BASE_COMMIT_HASH_KEY = "MergeTask.baseCommitHash";
    public static final String TARGET_COMMIT_HASH_KEY = "MergeTask.targetCommitHash";

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        try (Repository repo = jobWorkspaceService.getRepository(args.getJobId())) {
            Git git = new Git(repo);

            Ref targetRef = git.checkout()
                .setCreateBranch(true)
                .setName(targetBranch)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                .setStartPoint("origin/" + targetBranch)
                .call();

            git.checkout().setName(baseBranch).call();

            MergeResult result = git.merge()
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .include(targetRef)
                .setProgressMonitor(new SimpleLogProgressMonitor(messenger))
                .call();

            if (!result.getMergeStatus().isSuccessful()) {
                messenger.send(new LogMessage("Merge failed. status; " + result.getMergeStatus().name()));
                return false;
            }

            ObjectId[] mergedCommits = result.getMergedCommits();
            args.putData(BASE_COMMIT_HASH_KEY, mergedCommits[0].name());
            args.putData(TARGET_COMMIT_HASH_KEY, mergedCommits[1].name());
        }
        return true;
    }

    public void setBaseBranch(String baseBranch) {
        this.baseBranch = baseBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

}
