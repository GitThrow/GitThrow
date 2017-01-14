package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/{project}/pull-request")
public class PullRequestRestController {

    @Autowired
    private PullRequestUsecase pullRequestUsecase;

    private static final Logger logger = LoggerFactory.getLogger(PullRequestRestController.class);

    @GetMapping
    public List<PullRequest> all(Project project) {
        return pullRequestUsecase.findByProject(project);
    }

    @PostMapping
    public PullRequest create(@RequestBody PullRequest pullRequest, Project project, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        return pullRequestUsecase.create(project, pullRequest.getTitle(), pullRequest.getDescription(), pullRequest.getReviewer(), principal.getUser(), pullRequest.getBase(), pullRequest.getTarget());
    }

    @GetMapping("{prNum}")
    public PullRequest show(@PathVariable("prNum") int prNum, Project project) {
        return pullRequestUsecase.findById(project, prNum).orElseThrow(ResourceNotFoundException::new);
    }

    @PutMapping("{prNum}")
    public PullRequest update(@PathVariable("prNum") int prNum, Project project, @RequestBody PullRequest pullRequest) {
        return pullRequestUsecase.update(new PullRequest(project.getId(), prNum, pullRequest.getTitle(),
            pullRequest.getDescription(), pullRequest.getReviewer(), pullRequest.getProponent(),
            pullRequest.getBase(), pullRequest.getTarget(), pullRequest.getState()));
    }
}
