package net.orekyuu.gitthrow.pullrequest.util;

import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestComment;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestState;
import net.orekyuu.gitthrow.pullrequest.port.table.ClosedPullRequestTable;
import net.orekyuu.gitthrow.pullrequest.port.table.OpenPullRequestTable;
import net.orekyuu.gitthrow.pullrequest.port.table.PullRequestCommentTable;
import net.orekyuu.gitthrow.user.domain.model.User;

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

    public static PullRequestComment commentFromTable(PullRequestCommentTable table, User user) {
        return new PullRequestComment(table.getId().intValue(), table.getProjectId(), table.getText(), table.getCreatedAt(), user);
    }
}
