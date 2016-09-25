package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class ClosePullRequestTask implements Task {

    @Autowired
    private PullRequestService pullRequestService;

    private int prNum = -1;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        if (prNum < 0) {
            throw new IllegalArgumentException("prNumの値がおかしい");
        }

        String baseCommit = args.<String>getData(MergeTask.BASE_COMMIT_HASH_KEY)
            .orElseThrow(() -> new IllegalStateException("事前にMergeTaskが実行される必要があります。"));
        String targetCommit = args.<String>getData(MergeTask.TARGET_COMMIT_HASH_KEY)
            .orElseThrow(() -> new IllegalStateException("事前にMergeTaskが実行される必要があります。"));

        pullRequestService.close(args.getProjectId(), prNum, baseCommit, targetCommit);

        return true;
    }

    public void setPrNum(int prNum) {
        this.prNum = prNum;
    }
}
