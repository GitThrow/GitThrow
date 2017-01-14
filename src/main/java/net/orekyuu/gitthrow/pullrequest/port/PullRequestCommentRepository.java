package net.orekyuu.gitthrow.pullrequest.port;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestComment;
import net.orekyuu.gitthrow.pullrequest.port.table.PullRequestCommentDao;
import net.orekyuu.gitthrow.pullrequest.port.table.PullRequestCommentTable;
import net.orekyuu.gitthrow.pullrequest.util.PullRequestUtil;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PullRequestCommentRepository {

    private final PullRequestCommentDao commentDao;
    private final UserUsecase userUsecase;

    public PullRequestCommentRepository(PullRequestCommentDao commentDao, UserUsecase userUsecase) {
        this.commentDao = commentDao;
        this.userUsecase = userUsecase;
    }

    public PullRequestComment create(PullRequest pullRequest, String text, User user) {
        PullRequestCommentTable table = new PullRequestCommentTable(null, pullRequest.getProjectId(), (long)pullRequest.getPullrequestNum(), text, LocalDateTime.now(), user.getId());
        PullRequestCommentTable result = commentDao.insert(table).getEntity();
        return PullRequestUtil.commentFromTable(result, userUsecase.findById(result.getUserId()).orElse(null));
    }

    public List<PullRequestComment> findByPullRequest(PullRequest pr) {
        HashMap<String, User> userMap = new HashMap<>();
        return commentDao.findByProjectAndPrNum(pr.getProjectId(), pr.getPullrequestNum(),
            Collectors.mapping(table ->
                PullRequestUtil.commentFromTable(table,
                    userMap.computeIfAbsent(
                        table.getUserId(),
                        userId -> userUsecase.findById(userId).orElse(null)
                    )
                ),
                Collectors.toList()));
    }

    public void delete(PullRequestComment comment) {
        commentDao.delete(new PullRequestCommentTable((long)comment.getId(), null, null, null, null, null));
    }

    public void deleteByProject(Project project) {
        commentDao.deleteByProject(project.getId());
    }
}
