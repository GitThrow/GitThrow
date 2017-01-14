package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.git.FileDiffSender;
import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequestMapping("/project/{projectId}/git/diff")
public class GitController {

    @Autowired
    private FileDiffSender fileDiffSender;

    @ProjectMemberOnly
    @GetMapping
    public SseEmitter diff(@ProjectName @PathVariable("projectId") String projectId,
                           @RequestParam("base") String base,
                           @RequestParam("target") String target) throws GitAPIException, IOException {
        SseEmitter emitter = new SseEmitter(-1L);
        fileDiffSender.calcDiff(projectId, base, target, emitter);
        return emitter;
    }
}
