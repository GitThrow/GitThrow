package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.job.BuildJob;
import net.orekyuu.workbench.job.MergeJob;
import net.orekyuu.workbench.service.ProjectService;
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
    private ProjectService projectService;

    @GetMapping("/rest/job/build")
    public SseEmitter build(@RequestParam("projectId") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        SseEmitter emitter = new SseEmitter(-1L);
        BuildJob job = buildJob();
        job.setBranch("master");
        job.start(emitter, projectId, principal.getUser());
        return emitter;
    }

    @GetMapping("/rest/job/merge")
    public SseEmitter merge(@RequestParam("projectId") String projectId,
                            @RequestParam("base") String base,
                            @RequestParam("target") String target,
                            @RequestParam("prNum") int prNum,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) {

        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        SseEmitter emitter = new SseEmitter(-1L);
        MergeJob job = mergeJob();
        job.setTargetBranch(target);
        job.setBaseBranch(base);
        job.setClosePullRequestNum(prNum);
        job.start(emitter, projectId, principal.getUser());
        return emitter;
    }

    @Lookup
    BuildJob buildJob() {
        return null;
    }

    @Lookup
    MergeJob mergeJob() {
        return null;
    }
}
