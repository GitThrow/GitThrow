package net.orekyuu.gitthrow.pullrequest.usecase;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.port.PullRequestCommentRepository;
import net.orekyuu.gitthrow.pullrequest.port.PullRequestRepository;
import net.orekyuu.gitthrow.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PullRequestUsecase {

    private final PullRequestRepository pullRequestRepository;
    private final PullRequestCommentRepository commentRepository;

    public PullRequestUsecase(PullRequestRepository pullRequestRepository, PullRequestCommentRepository commentRepository) {
        this.pullRequestRepository = pullRequestRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = false)
    public PullRequest create(Project project, String title, String desc, User reviewer, User proponent, String base, String target) {
        return pullRequestRepository.create(project, title, desc, reviewer, proponent, base, target);
    }

    @Transactional(readOnly = false)
    public PullRequest update(PullRequest pullRequest) {
        return pullRequestRepository.save(pullRequest);
    }

    @Transactional(readOnly = false)
    public PullRequest merge(PullRequest pullRequest, String baseCommit, String targetCommit) {
        return pullRequestRepository.merge(pullRequest, baseCommit, targetCommit);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        pullRequestRepository.deleteByProject(project);
    }

    public List<PullRequest> findByProject(Project project) {
        return pullRequestRepository.findByProject(project);
    }

    public Optional<PullRequest> findById(Project project, int num) {
        return pullRequestRepository.findById(project, num);
    }
}
