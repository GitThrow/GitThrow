package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestComment;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestCommentUsecase;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/{project}/pull-request/{prNum}/comment")
public class PullRequestCommentRestController {

    @Autowired
    private PullRequestUsecase pullRequestUsecase;
    @Autowired
    private PullRequestCommentUsecase commentUsecase;

    @GetMapping
    public List<PullRequestComment> all(Project project, @PathVariable("prNum") int id) {
        PullRequest pr = pullRequestUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("PullRequest not found"));

        return commentUsecase.findByPullRequest(pr);
    }

    @PostMapping
    public PullRequestComment create(Project project,
                                @PathVariable("prNum") int id,
                                @RequestBody PullRequestComment comment,
                                @AuthenticationPrincipal WorkbenchUserDetails principal) {
        PullRequest pr = pullRequestUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("PullRequest not found"));
        return commentUsecase.create(pr, comment.getText(), principal.getUser());
    }
}
