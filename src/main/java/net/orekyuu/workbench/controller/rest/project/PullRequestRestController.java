package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.git.FileDiffSender;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/rest/pull-request")
public class PullRequestRestController {

    @Autowired
    private FileDiffSender fileDiffSender;

    private static final Logger logger = LoggerFactory.getLogger(PullRequestRestController.class);

    @GetMapping("/diff")
    public SseEmitter build(@RequestParam("projectId") String projectId,
                            @RequestParam("base") String base,
                            @RequestParam("target") String target,
                            @AuthenticationPrincipal WorkbenchUserDetails principal) throws GitAPIException, IOException {
        SseEmitter emitter = new SseEmitter(-1L);
        fileDiffSender.calcDiff(projectId, base, target, emitter);
        return emitter;
    }
}
