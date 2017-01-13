package net.orekyuu.gitthrow.pullrequest.usecase;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestComment;
import net.orekyuu.gitthrow.pullrequest.port.PullRequestCommentRepository;
import net.orekyuu.gitthrow.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PullRequestCommentUsecase {

    private final PullRequestCommentRepository commentRepository;

    public PullRequestCommentUsecase(PullRequestCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = false)
    public PullRequestComment create(PullRequest pr, String text, User user) {
        return commentRepository.create(pr, text, user);
    }

    @Transactional(readOnly = false)
    public void delete(PullRequestComment comment) {
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        commentRepository.deleteByProject(project);
    }

    public List<PullRequestComment> findByPullRequest(PullRequest pr) {
        return commentRepository.findByPullRequest(pr);
    }
}
