package net.orekyuu.workbench.pullrequest.util;

import net.orekyuu.workbench.pullrequest.domain.model.PullRequest;
import net.orekyuu.workbench.pullrequest.domain.model.PullRequestState;
import net.orekyuu.workbench.pullrequest.port.table.ClosedPullRequestTable;
import net.orekyuu.workbench.pullrequest.port.table.OpenPullRequestTable;
import net.orekyuu.workbench.user.domain.model.User;

public class PullRequestUtil {

    public static PullRequest fromTable(OpenPullRequestTable table, User reviewer, User proponent) {
        return new PullRequest(
            table.getProject(),
            table.getPrNum().intValue(),
            table.getTitle(),
            table.getDescription(),
            reviewer,
            proponent,
            table.getBaseBranch(),
            table.getTargetBranch(),
            PullRequestState.OPEN
        );
    }

    public static PullRequest fromTable(ClosedPullRequestTable table, User reviewer, User proponent) {
        return new PullRequest(
            table.getProject(),
            table.getPrNum().intValue(),
            table.getTitle(),
            table.getDescription(),
            reviewer,
            proponent,
            table.getBaseCommit(),
            table.getTargetCommit(),
            PullRequestState.MERGED
        );
    }
}
