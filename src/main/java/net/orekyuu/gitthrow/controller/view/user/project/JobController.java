package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import net.orekyuu.gitthrow.job.BuildJob;
import net.orekyuu.gitthrow.job.MergeJob;
import net.orekyuu.gitthrow.job.TestJob;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/project/{projectId}/job/")
public class JobController {

    @Autowired
    private ProjectUsecase projectService;

    @ProjectMemberOnly
    @GetMapping("build")
    public SseEmitter build(Project project,
                            @ProjectName @PathVariable("projectId") String projectId,
                            @RequestParam(value = "prNum", required = false) Integer prNum,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) {
        SseEmitter emitter = new SseEmitter(-1L);
        BuildJob job = buildJob();
        job.setHash("master");
        if (prNum != null) {
            job.setCommentTarget(prNum);
        }
        job.start(emitter, project, principal.getUser());
        return emitter;
    }

    @ProjectMemberOnly
    @GetMapping("test")
    public SseEmitter test(Project project,
                           @ProjectName @PathVariable("projectId") String projectId,
                           @RequestParam(value = "hash", required = false, defaultValue = "master") String hash,
                           @RequestParam(value = "prNum", required = false) Integer prNum,
                           @AuthenticationPrincipal WorkbenchUserDetails principal) {
        SseEmitter emitter = new SseEmitter(-1L);
        TestJob job = testJob();
        job.setHash(hash);
        if (prNum != null) {
            job.setPrNum(prNum);
        }
        job.start(emitter, project, principal.getUser());
        return emitter;
    }

    @ProjectMemberOnly
    @GetMapping("merge")
    public SseEmitter merge(Project project,
                            @ProjectName @PathVariable("projectId") String projectId,
                            @RequestParam("base") String base,
                            @RequestParam("target") String target,
                            @RequestParam("prNum") int prNum,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) {
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
