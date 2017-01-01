package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.job.BuildJob;
import net.orekyuu.workbench.job.MergeJob;
import net.orekyuu.workbench.job.TestJob;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class JobRestController {

    @Autowired
    private ProjectUsecase projectService;

    @GetMapping("/rest/job/build")
    public SseEmitter build(@RequestParam("projectId") String projectId,
                            @RequestParam(value = "prNum", required = false) Integer prNum,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }
        SseEmitter emitter = new SseEmitter(-1L);
        BuildJob job = buildJob();
        job.setHash("master");
        if (prNum != null) {
            job.setCommentTarget(prNum);
        }
        job.start(emitter, project, principal.getUser());
        return emitter;
    }

    @GetMapping("/rest/job/test")
    public SseEmitter test(@RequestParam("projectId") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }
        SseEmitter emitter = new SseEmitter(-1L);
        TestJob job = testJob();
        job.setHash("master");
        job.start(emitter, project, principal.getUser());
        return emitter;
    }

    @GetMapping("/rest/job/merge")
    public SseEmitter merge(@RequestParam("projectId") String projectId,
                            @RequestParam("base") String base,
                            @RequestParam("target") String target,
                            @RequestParam("prNum") int prNum,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) {

        Project project = projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }
        SseEmitter emitter = new SseEmitter(-1L);
        MergeJob job = mergeJob();
        job.setTargetBranch(target);
        job.setBaseBranch(base);
        job.setClosePullRequestNum(prNum);
        job.start(emitter, project, principal.getUser());
        return emitter;
    }

    @Lookup
    BuildJob buildJob() {
        return null;
    }

    @Lookup
    TestJob testJob() {
        return null;
    }

    @Lookup
    MergeJob mergeJob() {
        return null;
    }
}
